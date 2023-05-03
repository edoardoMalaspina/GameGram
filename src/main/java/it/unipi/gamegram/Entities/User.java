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
    private String email;
    private String password;

    public User(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User(String email){
        this.email = email;
    }

    public boolean signUp(String firstName, String lastName, String email, String Password){
        User newUser = new User(firstName, lastName, email, password);
        // controlla che non esista già utente con lo stesso email
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

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean checkCredentials (String email, String password) {
        try {
            MongoDBDriver md = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = md.getCollection("users");
            Document user = collection.find(eq("email", email)).first();
            if (user == null || !(email.equals(user.getString("email")) || !(password.equals(user.getString("password")))))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAdmin(String email) {
        MongoDBDriver driver = null;
        MongoCollection<Document> collection = null;

        try {
            driver = MongoDBDriver.getInstance();
            collection = driver.getCollection("users");
            MongoCursor<Document> cursor = collection.find(and(eq("email", email),eq("isadmin", "Yes"))).iterator();

            return(cursor.hasNext());

        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
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
    query neo4j creare nodo user: (da pensare se email ci va anche come proprietà o no)
    CREATE (email:User { firstname: "firstname", lastname: "lastname", email: "email"})

    query neo4j creare nodo game:
    CREATE (titoloGioco:Game { name: "name" })

    query neo4j linkare user-user con follow:
    CREATE (emailPartenza)-[:FOLLOW {date: dataDelFollow}]->(emailArrivo)

    query neo4j linkare user-game con like:
    CREATE (emailPartenza)-[:LIKE {date: dataDelLike}]->(titoloGioco)

    query neo4j linkare user-game con recensito:
    CREATE (emailPartenza)-[:REVIEWED {date: dataDellaReview, title: titoloDellaReview}]->(titoloGioco)

    query neo4j ottenere tutti gli utenti che un utente segue:
    MATCH (email:User)-[:FOLLOW]-(followed)
    WHERE email.firstname = "inserisciNome"
        AND email.lastname = "inserisciCognome"
        AND email.email = "inserisciemail"
    RETURN followed

    query neo4j ottenere tutti i giochi che piacciono ad un utente:
    MATCH (email:User)-[:LIKE]-(liked)
    WHERE email.firstname = "inserisciNome"
        AND email.lastname = "inserisciCognome"
        AND email.email = "inserisciemail"
    RETURN liked

    query neo4j ottenere tutti i giochi che un utente ha recensito:
    MATCH (email:User)-[:REVIEWED]-(reviewed)
    WHERE email.firstname = "inserisciNome"
        AND email.lastname = "inserisciCognome"
        AND email.email = "inserisciemail"
    RETURN reviewed

    query neo4j per smettere di followare un utente:
    MATCH (emailPartenza:User { firstname: "firstname", lastname: "lastname", email: "email"}) -[r:FOLLOW]->
            (emailArrivo:User { firstname: "firstname", lastname: "lastname", email: "email"})
    DELETE r

    query neo4j per togliere il like ad un gioco:
    MATCH (email:User { firstname: "firstname", lastname: "lastname", email: "email"}) -[r:LIKE]->
            (titoloGioco:Game { name: "name" })
    DELETE r
     */
}
