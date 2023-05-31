package it.unipi.gamegram.managersMongoDB;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.gamegram.drivers.MongoDBDriver;
import org.bson.Document;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserManagerMongoDB {

    public static boolean checkCredentials (String nick, String password) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = md.getCollection("users");
            Document user = collection.find(eq("nick", nick)).first();
            if (user == null || !(password.equals(user.getString("password"))))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkNick (String nick) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = md.getCollection("users");
            Document user = collection.find(eq("nick", nick)).first();
            if (user == null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Document findUserByNick(String nick) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("users");
            Document user = collection.find(eq("nick", nick)).first();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAdmin(String nick) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;
        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("users");
            MongoCursor<Document> cursor = collection.find(and(eq("nick", nick),eq("isadmin", "Yes"))).iterator();
            return(cursor.hasNext());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteUser(String nick) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            collection.deleteOne(eq("nick", nick));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(String nick, String password, String name, String surname) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            Document user;
            user = new Document("nick", nick)
                    .append("password", password)
                    .append("name", name)
                    .append("lastname", surname)
                    .append("isadmin", "No")
                    .append("reviews",new ArrayList<Document>());
            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            collection.insertOne(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
