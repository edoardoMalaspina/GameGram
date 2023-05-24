package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Record;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManagerNeo4j {
   // private final Neo4jDriver neo4jDBM;

    /*
    public UserManagerNeo4j(Neo4jDriver dbNeo4J) {
        this.neo4jDBM = dbNeo4J;
    }
     */

    public static ArrayList<User> getListFollowedUsers(User usr) {
        ArrayList<User> listFollowedUsers = new ArrayList<>();
        // DA TOGLIERE
        //Neo4jDriver TMP = new Neo4jDriver();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(followed:User) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                       // "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN followed.firstname, followed.lastname, followed.username");
                while (result.hasNext()) {
                    Record r = result.next();
                    listFollowedUsers.add(new User( r.get("followed.firstname").asString(), r.get("followed.lastname").asString(), r.get("followed.username").asString()));
                }
                return listFollowedUsers;
            });
            Neo4jDriver.closeNeo4J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFollowedUsers;
    }

    public static ArrayList<Game> getListLikedGames(User usr){
        // DA TOGLIERE
        //Neo4jDriver TMP = new Neo4jDriver();
        ArrayList<Game> listLikedGames = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                      //  "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN liked.name");
                while (result.hasNext()) {
                    Record r = result.next();
                    listLikedGames.add(new Game(r.get("liked.name").asString()));
                }
                return listLikedGames;
            });
            Neo4jDriver.closeNeo4J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikedGames;
    }

    private static ArrayList<Like> getLikedGameDated(User usr){
        ArrayList<Like> listLikes = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                        "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN liked.name, like.date");
                while (result.hasNext()) {
                    Record r = result.next();
                    String dateLikeString = r.get("like.date").asString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateLike = LocalDate.parse(dateLikeString, formatter);
                    long dayPassed = ChronoUnit.DAYS.between(today, dateLike);
                    listLikes.add(new Like(r.get("liked.name").asString(), dayPassed));
                }
                return listLikes;
            });
            Neo4jDriver.closeNeo4J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikes;
    }


    public static void addUserNode(User usr){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("CREATE (:User { username:'" + usr.getNick() + "', lastname:'" + usr.getLastName() + "', firstname:'" + usr.getFirstName() + "'})");
                return null;
            } );
            Neo4jDriver.closeNeo4J();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkFollow(User follower, User followed){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
           // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
           // String formattedDate = currentDate.format(formatter);
            if(checkIfAlreadyFollowed(follower, followed)){
                System.out.println("You are already following this user"); //vedere come sistemare con l'interfaccia grafica
                return;
            }
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+follower.getNick()+"'}), (n2 {username: '"+followed.getNick()+"'})" +
                        "CREATE (n1)-[:FOLLOW {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
            Neo4jDriver.closeNeo4J();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkReviewed(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
            //String formattedDate = currentDate.format(formatter);
            // check if already reviewed
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+rev.getAuthor()+"'}), (n2 {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED {date: '"+ currentDate +"', title: '"+rev.getTitle()+"'}]->(n2)");
                return null;
            } );
            Neo4jDriver.closeNeo4J();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkLike(User usr, Game game){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
           // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
           // String formattedDate = currentDate.format(formatter);
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
            Neo4jDriver.closeNeo4J();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static boolean checkIfAlreadyFollowed(User follower, User followed){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
                    "RETURN COUNT(*) > 0 as followExists");
            Neo4jDriver.closeNeo4J();
            return result.single().get("followExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkIfAlreadyLiked(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[:LIKE]->(n2 {name: '"+ game.getName() +"'})" +
                    "RETURN COUNT(*) > 0 as likeExists");
            Neo4jDriver.closeNeo4J();
            return result.single().get("likeExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkIfAlreadyReviewed(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
                    "RETURN COUNT(*) > 0 as reviewExists");
            Neo4jDriver.closeNeo4J();
            return result.single().get("reviewExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public static boolean unfollow(User follower, User followed){
        if ( !checkIfAlreadyFollowed(follower, followed) ){
            System.out.println( "You are not following this user" );
            return false;
        }
        else {
            try (Session session =  Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[follow:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
                        "DELETE follow");
                Neo4jDriver.closeNeo4J();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean unlike(User usr, Game game){
        if ( !checkIfAlreadyLiked(usr, game) ){
            System.out.println( "You are not liking this game" );
            return false;
        }
        else {
            try (Session session = Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '" + usr.getNick() + "'})-[like:LIKE]->(n2 {name: '" + game.getName() + "'})" +
                        "DELETE like");
                Neo4jDriver.closeNeo4J();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean cancelReview(User usr, Game game){
        if( !checkIfAlreadyReviewed(usr, game) ){
            System.out.println( "You have not written a review for this game" );
            return false;
        }
        else{
            try (Session session =  Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[review:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
                        "DELETE review");
                Neo4jDriver.closeNeo4J();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    // aggiungi controllo sul fatto che non sia un utente che segui già quello suggerito
    public static ArrayList<User> suggestFriend(User usr){
        // prendi la lista di tuoi amici
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // crea hashmap<Utente Integer>
        HashMap<User, Integer> mapSuggestedFriends = new HashMap<>();
        // scorri la lista di amici dei tuoi amici e aggiungi tutti gli utenti all'ashmap
        for (User user:listFollowed){
            ArrayList<User> listFollowedOfFriend = getListFollowedUsers(user);
            for (User tmp:listFollowedOfFriend){
                if (!mapSuggestedFriends.containsKey(tmp))
                    mapSuggestedFriends.put(tmp, 1);
                else{
                    // se un utente è già presente aggiungi 1 al suo value
                    int oldValue = mapSuggestedFriends.get(tmp);
                    mapSuggestedFriends.replace(tmp, oldValue, oldValue+1);
                }
            }
        }
        // ritorna i 5 users con il punteggio più alto
        ArrayList<User> result = new ArrayList<>();

        // DA FARE MEGLIO INDIVIDUANDO SENZA FARLA O(N^2)
        for (int i=0; i<5; i++) {
            User maximumKey = null;
            int maximumValue = 0;
            for (User key : mapSuggestedFriends.keySet()) {
                if (mapSuggestedFriends.get(key) > maximumValue) {
                    maximumValue = mapSuggestedFriends.get(key);
                    maximumKey = key;
                }
            }
            mapSuggestedFriends.remove(maximumKey);
            result.add(maximumKey);
        }
        return result;

    }

    // method that taken the list of followed users return the oldest like
    // da testare
    private static long getOldestLikeAmongFollowed(User usr){
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        long maximumDayPassed = 0;
        for (User tmp:listFollowed){
            ArrayList<Like> listLikes = getLikedGameDated(tmp);
            for(Like like:listLikes){
                if (like.dayPassedSinceLike > maximumDayPassed)
                    maximumDayPassed = like.dayPassedSinceLike;
            }
        }
        return maximumDayPassed;
    }

    // da testare
    private static double calculateScoreLike(long maximumDayPassed, long daysSinceLike){
        return Math.exp((maximumDayPassed - daysSinceLike) / maximumDayPassed);
    }

    // da testare
    public String suggestTrendingNowAmongFollowed(User usr){
        // prendi dal grafo la lista di amici
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // creiamo una struttura hashmap con keys: tutti i giochi a cui gli amici hanno messo like
        HashMap<String, Double> mapScores = new HashMap<>();
        long maximumDayPassed = getOldestLikeAmongFollowed(usr);
        for (User tmp:listFollowed){
            ArrayList<Like> listLikes = getLikedGameDated(tmp);
            for (Like like:listLikes){
                if(!mapScores.containsKey(like.nameOfTheGame))
                    mapScores.put(like.nameOfTheGame, calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
                else{
                    double oldScore = mapScores.get(like.nameOfTheGame);
                    mapScores.replace(like.nameOfTheGame, oldScore, oldScore + calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
                }
            }
        }
        // ora vediamo qual è il gioco con lo score più alto
        String bestTitle = " ";
        double bestScore = 0;
        for (String key:mapScores.keySet()){
            if(mapScores.get(key) > bestScore){
                bestScore = mapScores.get(key);
                bestTitle = key;
            }
        }
        return bestTitle;
    }

    private static class Like{
        String nameOfTheGame;
        long dayPassedSinceLike;

        private Like(String nameOfTheGame, long dayPassedSinceLike){
            this.nameOfTheGame = nameOfTheGame;
            this.dayPassedSinceLike = dayPassedSinceLike;
        }
    }

    public static void main(String[] args){

       // Neo4jDriver dbManager = new Neo4jDriver();
/*
        User usr1 = new User("a", "b", "d_rowe2583");
        User usr2 = new User("c", "d","m_linda3865");
        User usr3 = new User("e", "f","pluto");
        User usr4 = new User("g", "h", "niko_pandetta");
        User usr5 = new User("i", "l", "gennaro");


        addUserNode(usr1);
        addUserNode(usr2);
        addUserNode(usr3);
        addUserNode(usr4);
        addUserNode(usr5);

        LocalDate data = LocalDate.now();

        LocalDate dataa = LocalDate.now();
        Game pippo = new Game("paperino", "developer", dataa, 50);

        GameManagerNeo4j.addGameNode(pippo);
        addDirectedLinkLike(usr4, pippo);

        Review review = new Review(dataa, "niko_pandetta", "paperino", "aaaaaaaa");
        addDirectedLinkReviewed(review);

        addDirectedLinkFollow(usr4, usr3);
        addDirectedLinkFollow(usr4, usr1);
        addDirectedLinkFollow(usr2, usr1);
        addDirectedLinkFollow(usr5, usr2);
        addDirectedLinkFollow(usr5, usr4);

        /*
        User suggested = suggestFriend(usr5);
        System.out.println(suggested.getNick());
         */
        /*
        cancelReview(usr4, pippo);
        unfollow(usr4, usr3);
        */
        /*
        ArrayList<Game> list = getListLikedGames(usr4);
        for (Game game:list){
            System.out.println(game.getName());
        }

        ArrayList<Like> lista = getLikedGameDated(usr4);
        for (Like like:lista){
            System.out.println(like.nameOfTheGame + " " + like.dayPassedSinceLike);
        }

        unlike(usr4, pippo);


        User usr1 = new User("edoardo", "edoardo", "edoardo");
        User usr2 = new User("i", "l","gennaro");

        addUserNode(usr1);
        addUserNode(usr2);
        GameManagerNeo4j.addGameNode(pippo);
        GameManagerNeo4j.addGameNode(pluto);

        addDirectedLinkLike(usr2, pippo);
        addDirectedLinkLike(usr2, pluto);

        addDirectedLinkFollow(usr2, usr1);
        */

        User usr1 = new User("a", "a", "a");
        User usr2 = new User("b", "b","b");
        User usr3 = new User("c", "c","c");
        User usr4 = new User("d", "d", "d");
        User usr5 = new User("e", "e", "e");
        User usr6 = new User("f", "f", "f");
        User usr7 = new User("g", "g", "g");
        User usr8 = new User("h", "h", "h");
        User usr9 = new User("i", "i", "i");
        User usr10 = new User("l", "l", "l");
        User usr11 = new User("m", "m", "m");
        User usr12 = new User("n", "n", "n");

        addUserNode(usr1);
        addUserNode(usr2);
        addUserNode(usr3);
        addUserNode(usr4);
        addUserNode(usr5);
        addUserNode(usr6);
        addUserNode(usr7);
        addUserNode(usr8);
        addUserNode(usr9);
        addUserNode(usr10);
        addUserNode(usr11);
        addUserNode(usr12);

        addDirectedLinkFollow(usr1, usr2);
        addDirectedLinkFollow(usr1, usr3);
        addDirectedLinkFollow(usr1, usr4);
        addDirectedLinkFollow(usr2, usr5);
        addDirectedLinkFollow(usr2, usr6);
        addDirectedLinkFollow(usr2,usr7);
        addDirectedLinkFollow(usr3, usr8);
        addDirectedLinkFollow(usr4, usr3);
        addDirectedLinkFollow(usr4, usr9);
        addDirectedLinkFollow(usr4, usr10);
        addDirectedLinkFollow(usr2, usr10);
        addDirectedLinkFollow(usr3, usr10);
        addDirectedLinkFollow(usr2, usr11);
        addDirectedLinkFollow(usr2, usr12);

        ArrayList<User> suggested = suggestFriend(usr1);
        for (User tmp :suggested){
            System.out.println(tmp.getNick());
        }


    }


}




