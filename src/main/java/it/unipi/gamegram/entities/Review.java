package it.unipi.gamegram.entities;

import it.unipi.gamegram.utility.DateConverter;
import org.bson.Document;
import java.time.LocalDate;

public class Review {

    private String reviewText;
    private LocalDate reviewDate;
    private final String author;
    private final String gameOfReference;
    private String title;

    // Constructors
    public Review(String reviewText, LocalDate reviewDate, String author, String gameOfReference, String title) {
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public Review(String author, LocalDate reviewDate, String gameOfReference) {
        this.author = author;
        this.reviewDate = reviewDate;
        this.gameOfReference = gameOfReference;
    }

    public Review(Document document) {
        this.reviewText = (document.get("review_text") == null) ? "" : document.getString("review_text");
        this.author = (document.get("author") == null) ? "" : document.getString("author");
        this.gameOfReference = (document.get("game") == null) ? "" : document.getString("game");
        this.title = (document.get("review_title") == null) ? "" : document.getString("review_title");
        this.reviewDate = DateConverter.convertToLocalDate((document.get("review_date") == null)
                          ? null : document.getDate("review_date"));
    }

    public Review(LocalDate reviewDate, String author, String gameOfReference, String title) {
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public Review(String author, String gameOfReference) {
        this.author = author;
        this.gameOfReference = gameOfReference;
    }

    // Getters
    public String getReviewText() {
        return reviewText;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getGameOfReference() {
        return gameOfReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}