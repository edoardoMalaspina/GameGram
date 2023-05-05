package it.unipi.gamegram.Entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public Review(LocalDate reviewDate, String author, String gameOfReference, String title){
        this.reviewDate = reviewDate;
        this.author = author;
        this.gameOfReference = gameOfReference;
        this.title = title;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewDateFormatted(){
        LocalDate date = getReviewDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
        String formattedDate = date.format(formatter);
        return formattedDate;
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
}
