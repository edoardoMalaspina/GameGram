package it.unipi.gamegram.managersMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.gamegram.utility.DateConverter;
import it.unipi.gamegram.drivers.MongoDBDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

public class ReviewManagerMongoDB {

    public static List<Document> findReviewByAuthor(String author) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("users");
            Document user = collection.find(eq("nick", author)).first();
            List<Document> reviews = user.getList("reviews", Document.class);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteReview(String name, String author) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> userCollection = md.getCollection("users");
            MongoCollection<Document> gameCollection = md.getCollection("games");

            // Delete user review
            Bson userFilter = Filters.eq("nick", author);
            Bson userUpdate = Updates.pull("reviews", Filters.eq("game", name));
            userCollection.updateOne(userFilter, userUpdate);

            // Delete game review
            Bson gameFilter = Filters.eq("name", name);
            Bson gameUpdate = Updates.pull("reviews", Filters.eq("author", author));
            gameCollection.updateOne(gameFilter, gameUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertReview(String author, LocalDate localDate, String title, String text, String game) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> userCollection = md.getCollection("users");
            MongoCollection<Document> gameCollection = md.getCollection("games");
            Date date = DateConverter.convertLocalDateToDate(localDate);
            Document review = new Document("author", author)
                    .append("review_date", date)
                    .append("review_title", title)
                    .append("review_text", text)
                    .append("game", game);
            // Update user collection
            Bson userFilter = Filters.eq("nick", author);
            Bson userUpdate = Updates.push("reviews", review);
            userCollection.updateOne(userFilter, userUpdate);
            // Update game collection
            Bson gameFilter = Filters.eq("name", game);
            Bson gameUpdate = Updates.push("reviews", review);
            gameCollection.updateOne(gameFilter, gameUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean findReviewByGameAndAuthor(String game, String author) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            Document userDoc = collection.find(eq("nick", author)).first();
            List <Document> reviews = userDoc.getList("reviews", Document.class);
            for(Document review: reviews){
                if(review.getString("game").equals(game)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static List<Document> findReviewByGame(String name) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("games");
            Document game = collection.find(eq("name", name)).first();
            List<Document> reviews = game.getList("reviews", Document.class);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
