package it.unipi.gamegram.Entities;

import java.util.Date;

public class Review {

    private String reviewText;
    private String reviewDate; // valutare se mettera come tipo Date invece di String
    private String author;
    private String gameOfReference; // valutare se mettere come tipo Game invece di String
    private String title;

    public Review(String reviewText, String reviewDate, String author, String gameOfReference, String title){
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public Review(String reviewDate, String author, String gameOfReference, String title){
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

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
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
}
