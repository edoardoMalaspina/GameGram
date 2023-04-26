package com.example.gamegram;

import java.util.Date;

public class Game {
    private String name;
    private Date dateOfPubblication;
    private String developer;
    private int price;

    public Game(String name, Date dateOfPubblication, String developer, int price){
        this.name = name;
        this.dateOfPubblication = dateOfPubblication;
        this.developer = developer;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfPubblication() {
        return dateOfPubblication;
    }

    public void setDateOfPubblication(Date dateOfPubblication) {
        this.dateOfPubblication = dateOfPubblication;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
