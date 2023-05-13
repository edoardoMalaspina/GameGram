package it.unipi.gamegram.Entities;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.MongoDBDriver;
import org.bson.Document;

import java.time.LocalDate;

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
    private float price;
    private String shortDescription;
    private String fullDescription;

    public Game(String name){
        this.name = name;
    }

    public Game (String name, String developer, LocalDate dateOfPublication, float price){
        this.dateOfPublication = dateOfPublication;
        this.name = name;
        this.developer = developer;
        this.price = price;
    }

    public Game(String name, String shortDescription){
        this.name = name;
        this.shortDescription = shortDescription;
    }

    @SuppressWarnings("unchecked")
    public Game(Document document) {
       // this.dateOfPublication = (document.get("dateOfPublication") == null) ? null : document.getDate("dateOfPublication");
        this.name = (document.get("name") == null) ? "" : document.getString("name");
        this.developer = (document.get("developer") == null) ? "" : document.getString("developer");
        this.price = (document.get("price") == null) ? null : document.getLong("price");
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

    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    public float getPrice() {
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