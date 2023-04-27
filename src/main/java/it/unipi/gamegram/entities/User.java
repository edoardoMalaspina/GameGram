package it.unipi.gamegram.entities;

public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User(String firstName, String lastName, String username, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
    }

    public boolean signUp(String firstName, String lastName, String username, String Password){
        User newUser = new User(firstName, lastName, username, password);
        // controlla che non esista già utente con lo stesso username
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

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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
    query neo4j creare nodo user: (da pensare se username ci va anche come proprietà o no)
    CREATE (username:User { firstname: "firstname", lastname: "lastname", username: "username"})

    query neo4j creare nodo game:
    CREATE (titoloGioco:Game { name: "name" })

    query neo4j linkare user-user con follow:
    CREATE (usernamePartenza)-[:FOLLOW {date: dataDelFollow}]->(usernameArrivo)

    query neo4j linkare user-game con like:
    CREATE (usernamePartenza)-[:LIKE {date: dataDelLike}]->(titoloGioco)

    query neo4j linkare user-game con recensito:
    CREATE (usernamePartenza)-[:REVIEWED {date: dataDellaReview, title: titoloDellaReview}]->(titoloGioco)

    query neo4j ottenere tutti gli utenti che un utente segue:
    MATCH (username:User)-[:FOLLOW]-(followed)
    WHERE username.firstname = "inserisciNome"
        AND username.lastname = "inserisciCognome"
        AND username.username = "inserisciUsername"
    RETURN followed

    query neo4j ottenere tutti i giochi che piacciono ad un utente:
    MATCH (username:User)-[:LIKE]-(liked)
    WHERE username.firstname = "inserisciNome"
        AND username.lastname = "inserisciCognome"
        AND username.username = "inserisciUsername"
    RETURN liked

    query neo4j ottenere tutti i giochi che un utente ha recensito:
    MATCH (username:User)-[:REVIEWED]-(reviewed)
    WHERE username.firstname = "inserisciNome"
        AND username.lastname = "inserisciCognome"
        AND username.username = "inserisciUsername"
    RETURN reviewed

    query neo4j per smettere di followare un utente:
    MATCH (usernamePartenza:User { firstname: "firstname", lastname: "lastname", username: "username"}) -[r:FOLLOW]->
            (usernameArrivo:User { firstname: "firstname", lastname: "lastname", username: "username"})
    DELETE r

    query neo4j per togliere il like ad un gioco:
    MATCH (username:User { firstname: "firstname", lastname: "lastname", username: "username"}) -[r:LIKE]->
            (titoloGioco:Game { name: "name" })
    DELETE r
     */
}
