package it.unipi.gamegram.Entities;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.DateConverter;
import it.unipi.gamegram.MongoDBDriver;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Game {
    private String name;
    private String developer;
    private String publisher;
    private LocalDate dateOfPublication;
    private double price;
    private String shortDescription;
    private List<Document> reviews;

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
    public Game(Document document) {
        this.name = (document.get("name") == null) ? "" : document.getString("name");
        this.developer = (document.get("developer") == null) ? "" : document.getString("developer");
        this.price = (document.get("price") == null) ? 0 : document.getDouble("price");
        this.publisher = (document.get("publisher") == null) ? "" : document.getString("publisher");
        this.shortDescription = (document.get("shortDescription") == null) ? "" : document.getString("shortDescription");
        this.dateOfPublication = DateConverter.convertToLocalDate((document.get("dateOfPublication") == null) ? null :
                document.getDate("dateOfPublication"));
        this.reviews = (document.get("reviews") == null) ? new ArrayList<Document>() : document.getList("reviews", Document.class);
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

    public List<Document> getReviews() {
        return reviews;
    }

    public static void delete(String name) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("games");
            Game game = new Game(Game.findByName(name));
            for(Document reviewDoc: game.getReviews()){
                Review review = new Review(reviewDoc);
                review.delete(name, review.getAuthor());}
            collection.deleteOne(eq("name", name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert(String name, LocalDate dateOfPublication, String developer, String publisher,
                              double price, String shortDescription) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            Document game;
            Date date;

            date = DateConverter.convertLocalDateToDate(dateOfPublication);

            game = new Document("name", name)
                    .append("dateOfPublication", date)
                    .append("developer", developer)
                    .append("publisher", publisher)
                    .append("price", price)
                    .append("shortDescription", shortDescription);

            md = MongoDBDriver.getInstance();
            collection = md.getCollection("games");
            collection.insertOne(game);

            //createUserNode(registNick);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Double getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
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