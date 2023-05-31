package it.unipi.gamegram.entities;

import it.unipi.gamegram.utility.DateConverter;
import org.bson.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final String name;
    private String developer;
    private String publisher;
    private LocalDate dateOfPublication;
    private double price;
    private String shortDescription;
    private String fullDescription;
    private List < Document > reviews;

    // Constructors

    public Game(String name) {
        this.name = name;
    }

    public Game(String name, String developer, LocalDate dateOfPublication, double price) {
        this.dateOfPublication = dateOfPublication;
        this.name = name;
        this.developer = developer;
        this.price = price;
    }

    public Game(String name, String shortDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public Game(Document document) {
        // Initialize the Game object from a MongoDB Document
        this.name = (document.get("name") == null) ? "" : document.getString("name");
        this.developer = (document.get("developer") == null) ? "" : document.getString("developer");
        this.price = (document.get("price") == null) ? 0 : document.getDouble("price");
        this.publisher = (document.get("publisher") == null) ? "" : document.getString("publisher");
        this.shortDescription = (document.get("shortDescription") == null) ? "" : document.getString("shortDescription");
        this.fullDescription = (document.get("fullDescription") == null) ? "" : document.getString("fullDescription");
        this.dateOfPublication = DateConverter.convertToLocalDate((document.get("dateOfPublication") == null) ? null :
                document.getDate("dateOfPublication"));
        this.reviews = (document.get("reviews") == null) ? new ArrayList < > () : document.getList("reviews", Document.class);
    }

    // Getters

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    public double getPrice() {
        return price;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public List < Document > getReviews() {
        return reviews;
    }
}