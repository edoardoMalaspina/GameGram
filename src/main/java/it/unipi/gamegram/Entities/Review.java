package it.unipi.gamegram.Entities;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.MongoDBDriver;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static it.unipi.gamegram.Entities.Game.convertToLocalDate;

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

    public Review(Document document) {
        this.reviewText = (document.get("review_text") == null) ? "" : document.getString("review_text");
        this.author = (document.get("author") == null) ? "" : document.getString("author");
        this.gameOfReference = (document.get("game") == null) ? "" : document.getString("game");
        this.title = (document.get("review_title") == null) ? "" : document.getString("review_title");
        this.reviewDate = convertToLocalDate((document.get("review_date") == null) ? null : document.getDate("review_date"));
    }

    public Review(LocalDate reviewDate, String author, String gameOfReference, String title){
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
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
        List<Document> reviews = new ArrayList<Document>();
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("reviews");
            for (Document d:collection.find(eq("author", author))) {
                reviews.add(d);
            }
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
