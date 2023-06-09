package it.unipi.gamegram.entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.gamegram.drivers.MongoDBDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private String nick;
    private String isAdmin;
    private List < Document > reviews;
    private String password;

    // Constructors
    public User(String firstName, String lastName, String nick, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
    }

    public User(String nick) {
        this.nick = nick;
    }

    public User(String firstName, String lastName, String nick) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
    }

    public User(Document document) {
        this.nick = (document.get("nick") == null) ? "" : document.getString("nick");
        this.firstName = (document.get("name") == null) ? "" : document.getString("name");
        this.lastName = (document.get("lastname") == null) ? "" : document.getString("lastname");
        this.isAdmin = (document.get("isadmin") == null) ? "" : document.getString("isadmin");
        this.reviews = (document.get("reviews") == null) ? new ArrayList<>() : document.getList("reviews", Document.class);
        this.password = (document.get("password") == null) ? "" : document.getString("password");
    }



    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNick() {
        return nick;
    }

    public List < Document > getReviews() {
        return reviews;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Method to promote a user to admin
    public void promoteAdmin() {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection < Document > userCollection = md.getCollection("users");
            Bson userFilter = Filters.eq("nick", this.getNick());
            Bson userUpdate = Updates.set("isadmin", "Yes");
            userCollection.updateOne(userFilter, userUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}