package it.unipi.gamegram.Entities;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.gamegram.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.security.MessageDigest;

public class User {

    private String firstName;
    private String lastName;
    private String nick;
    private String password;

    public User(String firstName, String lastName, String nick, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.nick = nick;
    }

    public User(String nick){
        this.nick = nick;
    }

    public boolean signUp(String firstName, String lastName, String nick, String Password){
        User newUser = new User(firstName, lastName, nick, password);
        // controlla che non esista già utente con lo stesso nick
        // nella collezione su MongoDB
        // se non esiste:
        // aggiungi alla collezione su MongoDB il nuovo utente
        // aggiungi nuovo nodo su Neo4j
        return true;
        // se esisteva già utente con lo stesso nome
       // return false;
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
            if (user == null || !(nick.equals(user.getString("nick")) || !(password.equals(user.getString("password")))))
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

    public static void register(String nick, String password, String name, String surname) {
        try {
            MongoDBDriver md;
            MongoCollection<Document> collection;
            Document user;

            user = new Document("nick", nick)
                    .append("password", password)
                    .append("name", name)
                    .append("surname", surname)
                    .append("isadmin", "No");

            md = MongoDBDriver.getInstance();
            collection = md.getCollection("users");
            collection.insertOne(user);

            //createUserNode(registNick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void suggestTrendingNowAmongFollowed(){
        // prendi dal grafo la lista di amici
        // creiamo una struttura hashmap con keys: tutti i giochi a cui gli amici hanno messo like
        // vedi qual è il like più vecchio messo e salva di quanti giorni è vecchio (es. int firstLike = 153)
        // ora scorriamo ogni amico e aggiungiamo i pesi dei giochi a cui lui ha messo like:
        //      il peso aggiornato sarà: pesoAttuale+nuovoPeso
        //      dove nuovoPeso = e^[(firstLike-daysLike)/firstLike] (daysLike sarebbe da quanti giorni è stato messo il like che stiamo guardando ora)
        // finito di aggiornare tutti i pesi ritorni il titolo del gioco con il peso maggiore

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
