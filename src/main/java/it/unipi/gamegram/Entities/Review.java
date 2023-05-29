package it.unipi.gamegram.Entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.gamegram.DateConverter;
import it.unipi.gamegram.MongoDBDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Review {

    private String reviewText;
    private LocalDate reviewDate; // valutare se mettera come tipo Date invece di String
    private String author;
    private String gameOfReference; // valutare se mettere come tipo Game invece di String
    private String title;

    public Review(String reviewText, LocalDate reviewDate, String author, String gameOfReference, String title){
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public Review(String author, LocalDate reviewDate, String gameOfReference){
        this.author = author;
        this.reviewDate = reviewDate;
        this.gameOfReference = gameOfReference;
    }

    public Review(Document document) {
        this.reviewText = (document.get("review_text") == null) ? "" : document.getString("review_text");
        this.author = (document.get("author") == null) ? "" : document.getString("author");
        this.gameOfReference = (document.get("game") == null) ? "" : document.getString("game");
        this.title = (document.get("review_title") == null) ? "" : document.getString("review_title");
        this.reviewDate = DateConverter.convertToLocalDate((document.get("review_date") == null) ? null : document.getDate("review_date"));
    }

    public Review(LocalDate reviewDate, String author, String gameOfReference, String title){
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public static Boolean findByGameAndAuthor(String game, String author) {
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

    public String getReviewText() {
        return reviewText;
    }


    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGameOfReference() {
        return gameOfReference;
    }

    public void setGameOfReference(String gameOfReference) {
        this.gameOfReference = gameOfReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static List<Document> findByAuthor(String author) {
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

    public static List<Document> deleteUserReview(String name, String author) {
        User user = new User(User.findByNick(author));
        List <Document> reviews = user.getReviews();
        for(Document reviewDoc: reviews) {
            if (reviewDoc.getString("game").equals(name))
                reviews.remove(reviewDoc);
        }
        return reviews;
    }

    public static List<Document> deleteGameReview(String name, String author) {
        Game game = new Game(Game.findByName(name));
        List <Document> reviews = game.getReviews();
            for(Document reviewDoc: reviews) {
                if (reviewDoc.getString("author").equals(author)){
                    reviews.remove(reviewDoc);
                }
            }
        return reviews;
    }
    public static void delete(String name, String author) {
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


    public static void insert(String author, LocalDate localDate, String title, String text, String game) {
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



    public static List<Document> findByGame(String name) {
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
