package it.unipi.gamegram.managersMongoDB;

import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.drivers.MongoDBDriver;
import org.bson.Document;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class UserManagerMongoDB {

    // Check if credentials provided for logging in are correct
    public static boolean checkCredentials(String nick, String password) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = md.getCollection("users");
            Document user = collection.find(eq("nick", nick)).first();
            if (user == null || !(password.equals(user.getString("password"))))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Checks if a nick is present in the collection
    public static boolean checkNick(String nick) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection < Document > collection = md.getCollection("users");
            Document user = collection.find(eq("nick", nick)).first();
            if (user == null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Finds a user by its unique nickname
    public static Document findUserByNick(String nick) {
        MongoDBDriver driver;
        MongoCollection < Document > collection;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("users");
            return collection.find(eq("nick", nick)).first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete a user from MongoDB's collection
    public static void deleteUser(String nick) {
        try {
            MongoDBDriver md;
            MongoCollection < Document > collection;
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            collection.deleteOne(eq("nick", nick));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add a user into the MongoDB collections from the signup form
    public static void register(String nick, String password, String name, String surname) {
        try {
            MongoDBDriver md;
            MongoCollection < Document > collection;
            Document user;
            user = new Document("nick", nick)
                    .append("password", password)
                    .append("name", name)
                    .append("lastname", surname)
                    .append("isadmin", "No")
                    .append("reviews", new ArrayList < Document > ());
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            collection.insertOne(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}