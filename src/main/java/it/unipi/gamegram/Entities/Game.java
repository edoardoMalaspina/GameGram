package it.unipi.gamegram.Entities;

import org.bson.Document;
import java.util.Date;

public class Game {
    private String name;
    private String developer;
    private Date dateOfPublication;
    private float price;
    private String shortDescription;
    private String fullDescription;

    public Game(String name){
        this.name = name;
    }

    public Game (String name, String developer, Date dateOfPublication, float price){
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
        this.dateOfPublication = (document.get("dateOfPublication") == null) ? null : document.getDate("dateOfPublication");
        this.name = (document.get("name") == null) ? "" : document.getString("name");
        this.developer = (document.get("developer") == null) ? "" : document.getString("developer");
        this.price = (document.get("price") == null) ? null : document.getLong("price");
    }

    public Date getDateOfPublication() {
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

    public void setDateOfPublication(Date dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShortDescription(){
        return shortDescription;
    }


}