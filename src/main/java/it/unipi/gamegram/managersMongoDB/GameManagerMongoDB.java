package it.unipi.gamegram.managersMongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.gamegram.utility.DateConverter;
import it.unipi.gamegram.drivers.MongoDBDriver;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.Review;
import org.bson.Document;
import org.jfree.data.category.DefaultCategoryDataset;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
public class GameManagerMongoDB {

    // Method to retrieve a game in the collection by its unique name
    public static Document findGameByName(String name) {
        MongoDBDriver driver;
        MongoCollection < Document > collection;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("games");
            return collection.find(eq("name", name)).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to delete a game from the collection (and its reviews from the relative users)
    public static void deleteGame(String name) {
        try {
            MongoDBDriver md;
            MongoCollection < Document > collection;
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("games");
            Game game = new Game(GameManagerMongoDB.findGameByName(name));
            for (Document reviewDoc: game.getReviews()) {
                Review review = new Review(reviewDoc);
                ReviewManagerMongoDB.deleteReview(name, review.getAuthor());
            }
            collection.deleteOne(eq("name", name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to insert a game in the collection
    public static void insertGame(String name, LocalDate dateOfPublication, String developer, String publisher,
                                  double price, String shortDescription, String fullDescription) {
        try {
            MongoDBDriver md;
            MongoCollection < Document > collection;
            Document game;
            Date date;
            date = DateConverter.convertLocalDateToDate(dateOfPublication);
            game = new Document("name", name)
                    .append("dateOfPublication", date)
                    .append("developer", developer)
                    .append("publisher", publisher)
                    .append("price", price)
                    .append("shortDescription", shortDescription)
                    .append("fullDescription", fullDescription);
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("games");
            collection.insertOne(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to check that a game's name is present in the collection
    public static boolean checkGameName(String name) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = md.getCollection("games");
            Document game = collection.find(eq("name", name)).first();
            if (game == null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static DefaultCategoryDataset priceTrend() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = driver.getCollection("games");

            // Construct the aggregation pipeline
            List < Document > pipeline = Arrays.asList(
                    new Document("$match", new Document()
                            .append("dateOfPublication", new Document("$gte", new Date(2000 - 1900, Calendar.JANUARY, 1)))
                            .append("dateOfPublication", new Document("$lte", new Date(2022 - 1900, Calendar.DECEMBER, 31)))
                    ),
                    new Document("$group", new Document("_id", new Document("year",
                            new Document("$dateToString", new Document("format", "%Y").append("date", "$dateOfPublication"))))
                            .append("averagePrice", new Document("$avg", "$price"))),
                    new Document("$sort", new Document("_id.year", 1))
            );

            // Execute the aggregation pipeline
            MongoCursor < Document > cursor = collection.aggregate(pipeline).iterator();

            // Create the dataset for the histogram
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            while (cursor.hasNext()) {
                Document result = cursor.next();

                // Access the result fields
                Document yearObj = result.get("_id", Document.class);
                String year = yearObj.getString("year");
                double averagePrice = result.getDouble("averagePrice");

                // Add data to the dataset
                dataset.addValue(averagePrice, "Average Price", year);
            }
            cursor.close();
            return dataset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DefaultCategoryDataset mostReviewedPerYear() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = driver.getCollection("games");

            // MongoDB Pipeline
            List<Document> pipeline = Arrays.asList(
                    new Document("$unwind", "$reviews"),
                    new Document("$addFields", new Document("reviewYear", new Document("$year", "$reviews.review_date"))),
                    new Document("$group", new Document("_id", new Document("year", "$reviewYear")
                            .append("game", "$name"))
                            .append("reviewCount", new Document("$sum", 1))),
                    new Document("$sort", new Document("_id.year", 1).append("reviewCount", -1)),
                    new Document("$group", new Document("_id", "$_id.year")
                            .append("mostReviewedGame", new Document("$first", "$_id.game"))
                            .append("maxReviewCount", new Document("$first", "$reviewCount"))),
                    new Document("$sort", new Document("_id", 1))
            );

            // Execution
            AggregateIterable<Document> result = collection.aggregate(pipeline);

            // Create the dataset for the bar chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Document doc : result) {
                int year = doc.getInteger("_id");
                String gameName = doc.getString("mostReviewedGame");
                int reviewCount = doc.getInteger("maxReviewCount");

                dataset.addValue(reviewCount, gameName, String.valueOf(year));
            }
            return dataset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}