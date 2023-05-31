package it.unipi.gamegram.Entities;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.gamegram.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class User {

    private String firstName;
    private String lastName;
    private String nick;
    private String password;
    private String isAdmin;
    private List<Document> reviews;

    public User(String firstName, String lastName, String nick, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.nick = nick;
    }

    public User(String nick){
        this.nick = nick;
    }

    public User(String firstName, String lastName, String nick){
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
    }

    public User(Document document) {
        this.nick = (document.get("nick") == null) ? "" : document.getString("nick");
        this.firstName = (document.get("name") == null) ? "" : document.getString("name");
        this.lastName = (document.get("lastname") == null) ? "" : document.getString("lastname");
        this.isAdmin = (document.get("isadmin") == null) ? "" : document.getString("isadmin");
        this.reviews = (document.get("reviews") == null) ? new ArrayList<Document>() : document.getList("reviews", Document.class);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNick() {
        return nick;
    }

    public List<Document> getReviews() {
        return reviews;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getIsAdmin() {
        return isAdmin;
    }

    public static Document findByNick (String nick) {
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

    public static void delete(String nick) {
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

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void promoteAdmin() {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> userCollection = md.getCollection("users");
            Bson userFilter = Filters.eq("nick", this.getNick());
            Bson userUpdate = Updates.set("isadmin", "Yes");
            userCollection.updateOne(userFilter, userUpdate);
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

            //createUserNode(registNick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
    query neo4j creare nodo user: (da pensare se nick ci va anche come proprietà o no)
    CREATE (nick:User { firstname: "firstname", lastname: "lastname", nick: "nick"})

    query neo4j creare nodo game:
    CREATE (titoloGioco:Game { name: "name" })

    query neo4j linkare user-user con follow:
    CREATE (nickPartenza)-[:FOLLOW {date: dataDelFollow}]->(nickArrivo)

    query neo4j linkare user-game con like:
    CREATE (nickPartenza)-[:LIKE {date: dataDelLike}]->(titoloGioco)

    query neo4j linkare user-game con recensito:
    CREATE (nickPartenza)-[:REVIEWED {date: dataDellaReview, title: titoloDellaReview}]->(titoloGioco)

    query neo4j ottenere tutti gli utenti che un utente segue:
    MATCH (nick:User)-[:FOLLOW]-(followed)
    WHERE nick.firstname = "inserisciNome"
        AND nick.lastname = "inserisciCognome"
        AND nick.nick = "inseriscinick"
    RETURN followed

    query neo4j ottenere tutti i giochi che piacciono ad un utente:
    MATCH (nick:User)-[:LIKE]-(liked)
    WHERE nick.firstname = "inserisciNome"
        AND nick.lastname = "inserisciCognome"
        AND nick.nick = "inseriscinick"
    RETURN liked

    query neo4j ottenere tutti i giochi che un utente ha recensito:
    MATCH (nick:User)-[:REVIEWED]-(reviewed)
    WHERE nick.firstname = "inserisciNome"
        AND nick.lastname = "inserisciCognome"
        AND nick.nick = "inseriscinick"
    RETURN reviewed

    query neo4j per smettere di followare un utente:
    MATCH (nickPartenza:User { firstname: "firstname", lastname: "lastname", nick: "nick"}) -[r:FOLLOW]->
            (nickArrivo:User { firstname: "firstname", lastname: "lastname", nick: "nick"})
    DELETE r

    query neo4j per togliere il like ad un gioco:
    MATCH (nick:User { firstname: "firstname", lastname: "lastname", nick: "nick"}) -[r:LIKE]->
            (titoloGioco:Game { name: "name" })
    DELETE r
     */
}
