package it.unipi.gamegram.Entities;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.MongoDBDriver;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class Game {
    private String name;

    public String getCategories() {
        return categories;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    private String categories;
    private String developer;
    private String publisher;
    private LocalDate dateOfPublication;
    private double price;
    private String shortDescription;
    private String fullDescription;

    public Game(String name){
        this.name = name;
    }

    public Game (String name, String developer, LocalDate dateOfPublication, double price){
        this.dateOfPublication = dateOfPublication;
        this.name = name;
        this.developer = developer;
        this.price = price;
    }

    public Game(String name, String shortDescription){
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public static LocalDate convertToLocalDate(Date date) {
        if(date == null){
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Game(Document document) {
        this.name = (document.get("name") == null) ? "" : document.getString("name");
        this.developer = (document.get("developer") == null) ? "" : document.getString("developer");
        this.price = (document.get("price") == null) ? 0 : document.getDouble("price");
        this.categories = (document.get("categories") == null) ? "" : document.getString("categories");
        this.publisher = (document.get("publisher") == null) ? "" : document.getString("publisher");
        this.shortDescription = (document.get("shortDescription") == null) ? "" : document.getString("shortDescription");
        this.fullDescription = (document.get("fullDescription") == null) ? "" : document.getString("fullDescription");
        this.dateOfPublication = convertToLocalDate((document.get("dateOfPublication") == null) ? null : document.getDate("dateOfPublication"));
    }

    public static Document findByName (String name) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("games");
            Document game = collection.find(eq("name", name)).first();
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkName (String name) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = md.getCollection("games");
            Document game = collection.find(eq("name", name)).first();
            if (game == null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStringDateOfPublication() {
        if(dateOfPublication == null){
            return "Date is missing.";
        }
        return dateOfPublication.toString();
    }

    public Double getPrice() {
        return price;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription(){
        return shortDescription;
    }


}