package it.unipi.gamegram.test;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.drivers.MongoDBDriver;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import org.bson.Document;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestPipelinesMongoDB {
    public static void main(String[] args) {
        priceTrendTest();
        mostReviewedPerYearTest();
        userReviewsTrendTest();
    }

    public static void priceTrendTest() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = driver.getCollection("games");

            DefaultCategoryDataset dataset = GameManagerMongoDB.priceTrend();
            if (dataset != null) {
                System.out.println("Average Price for games in 2022 calculated with pipeline: " +
                        dataset.getValue(dataset.getRowKey(0), dataset.getColumnKey(dataset.getColumnCount() - 1)));
            }
            // Define the target year
            int targetYear = 2022;

            // Create the start and end dates for the target year
            Calendar cal = Calendar.getInstance();
            cal.set(targetYear, Calendar.JANUARY, 1, 0, 0, 0);
            Date startDate = cal.getTime();
            cal.set(targetYear + 1, Calendar.JANUARY, 1, 0, 0, 0);
            Date endDate = cal.getTime();

            // Query for games from the target year
            Document query = new Document();
            query.put("dateOfPublication", new Document("$gte", startDate).append("$lt", endDate));
            FindIterable < Document > results = collection.find(query);

            // Calculate the average price of games from the target year
            double total = 0.0;
            int count = 0;
            for (Document game : results) {
                double price = game.getDouble("price");
                total += price;
                count++;
            }

            // Calculate the average price only if there are games from the target year
            double averagePrice = 0.0;
            if (count != 0) {
                averagePrice = total / count;
            }

            // Print the average price
            System.out.println("Average price of games from " + targetYear + " calculated by hand: " + averagePrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mostReviewedPerYearTest() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = driver.getCollection("games");

            DefaultCategoryDataset dataset = GameManagerMongoDB.mostReviewedPerYear();
            if (dataset != null) {
                System.out.println("Most reviewed game in 2018 calculated with pipeline: " +
                        dataset.getRowKey(5));
            }

            int targetYear = 2018;
            // Create the start and end dates for the target year
            Calendar cal = Calendar.getInstance();
            cal.set(targetYear, Calendar.JANUARY, 1, 0, 0, 0);
            Date startDate = cal.getTime();
            cal.set(targetYear + 1, Calendar.JANUARY, 1, 0, 0, 0);
            Date endDate = cal.getTime();

            // Query for games from the target year
            Document query = new Document();
            query.put("dateOfPublication", new Document("$gte", startDate).append("$lt", endDate));
            FindIterable < Document > results = collection.find(query);

            // Track the review count for each game
            Map < String, Integer > reviewCountMap = new HashMap < > ();

            // Iterate over the results and count the reviews for each game
            for (Document game : results) {
                int reviewCount = game.getList("reviews", Document.class).size();
                reviewCountMap.put(game.getString("name"), reviewCount);
            }

            // Find the game with the maximum review count
            Map.Entry < String, Integer > maxEntry = null;
            for (Map.Entry < String, Integer > entry: reviewCountMap.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }
            String mostReviewedGameName = null;
            if (maxEntry != null) {
                mostReviewedGameName = maxEntry.getKey();
            }
            System.out.println("Most reviewed game in " + targetYear + " calculated by hand: " + mostReviewedGameName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userReviewsTrendTest() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = driver.getCollection("users");
            User user = new User(UserManagerMongoDB.findUserByNick("robertson"));
            DefaultCategoryDataset dataset = UserManagerMongoDB.userReviewsTrend(user);
            System.out.println("Pipeline result for chosen user");
            if (dataset != null) {
                for (int i = 0; i < dataset.getRowCount(); i++) {
                    for (int j = 0; j < dataset.getColumnCount(); j++) {
                        System.out.println("User: " + user.getNick() + ", year: " + dataset.getColumnKey(j) + ", number of reviews: " + dataset.getValue(i, j));
                    }
                }
            }

            Map < Integer, Integer > reviewCountByYear = new HashMap < > ();

            // Query for the user by username
            Document query = new Document("nick", user.getNick());
            FindIterable < Document > results = collection.find(query);

            // Iterate over the results and count the reviews by year
            for (Document userDoc : results) {
                Object reviewsObj = userDoc.get("reviews");
                if (reviewsObj instanceof Iterable reviews) {
                    for (Object reviewObj : reviews) {
                        if (reviewObj instanceof Document review) {
                            Date reviewDate = review.getDate("review_date");
                            if (reviewDate != null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(reviewDate);
                                int year = cal.get(Calendar.YEAR);
                                reviewCountByYear.put(year, reviewCountByYear.getOrDefault(year, 0) + 1);
                            }
                        }
                    }
                }
            }
            System.out.println("Results found by hand for chosen user");
            for (Map.Entry < Integer, Integer > entry: reviewCountByYear.entrySet()) {
                int year = entry.getKey();
                int reviewCount = entry.getValue();
                System.out.println("User: " + user.getNick() + ", year: " + year + ", number of reviews: " + reviewCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}