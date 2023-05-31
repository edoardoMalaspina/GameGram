package it.unipi.gamegram.managersMongoDB;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.utility.DateConverter;
import it.unipi.gamegram.drivers.MongoDBDriver;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.Review;
import org.bson.Document;
import java.time.LocalDate;
import java.util.Date;
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

}