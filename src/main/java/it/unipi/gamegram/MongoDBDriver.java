package it.unipi.gamegram;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBDriver {

    private static MongoDBDriver driver = null;
    private MongoClient client;
    private MongoDatabase database;

    private MongoDBDriver() {
        client = MongoClients.create("mongodb://10.1.1.12:27017/");
        database = client.getDatabase("GameGram");
    }

    public static MongoDBDriver getInstance() {
        if(driver == null)
            driver = new MongoDBDriver();

        return driver;
    }

    public MongoCollection<Document> getCollection(String collection) {
        if(driver == null)
            throw new RuntimeException("No driver instance.");
        else
            return driver.database.getCollection(collection);
    }

    public static void close() {
        if(driver == null)
            throw new RuntimeException("No driver instance.");
        else
            driver.client.close();
    }

}
