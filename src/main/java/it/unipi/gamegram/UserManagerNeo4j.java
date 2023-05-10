package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Record;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserManagerNeo4j {
    private final Neo4jDbManager neo4jDBM;

    public UserManagerNeo4j(Neo4jDbManager dbNeo4J) {
        this.neo4jDBM = dbNeo4J;
    }

    public static ArrayList<User> getListFollowedUsers(User usr) {
        ArrayList<User> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDbManager.getDriver().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(followed:User) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                        "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN followed.firstname, followed.lastname, followed.username");
                while (result.hasNext()) {
                    Record r = result.next();
                    listFollowedUsers.add(new User( r.get("followed.firstname").asString(), r.get("followed.lastname").asString(), r.get("followed.username").asString()));
                }
                return listFollowedUsers;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFollowedUsers;
    }

    public static ArrayList<Game> getListLikedGames(User usr){
        ArrayList<Game> listLikedGames = new ArrayList<>();
        try (Session session = Neo4jDbManager.getDriver().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                        "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN liked.name");
                while (result.hasNext()) {
                    Record r = result.next();
                    System.out.println(r.get("liked.name").asString());
                    listLikedGames.add(new Game(r.get("liked.name").asString()));
                }
                return listLikedGames;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikedGames;
    }

    public static ArrayList<Like> getLikedGameDated(User usr){
        ArrayList<Like> listLikes = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try (Session session = Neo4jDbManager.getDriver().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.firstname = '" + usr.getFirstName() +
                        "' AND u.lastname = '" + usr.getLastName() +
                        "' AND u.username = '" + usr.getNick() +
                        "' RETURN liked.name, like.date");
                while (result.hasNext()) {
                    Record r = result.next();
                    LocalDate dateLike = r.get("like.date").asLocalDate();
                    long dayPassed = ChronoUnit.DAYS.between(today, dateLike);
                    listLikes.add(new Like(r.get("liked.name").asString(), dayPassed));
                }
                return listLikes;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikes;
    }

    public static void addUserNode(User usr){
        try(Session session= Neo4jDbManager.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("CREATE (:User { username:'" + usr.getNick() + "', lastname:'" + usr.getLastName() + "', firstname:'" + usr.getFirstName() + "'})");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkFollow(User follower, User followed){
        try(Session session= Neo4jDbManager.getDriver().session()){
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
            String formattedDate = currentDate.format(formatter);
            if(checkIfAlreadyFollowed(follower, followed)){
                System.out.println("You are already following this user"); //vedere come sistemare con l'interfaccia grafica
                return;
            }
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+follower.getNick()+"'}), (n2 {username: '"+followed.getNick()+"'})" +
                        "CREATE (n1)-[:FOLLOW {date: '"+ formattedDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkReviewed(Review rev){
        try(Session session= Neo4jDbManager.getDriver().session()){
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
            String formattedDate = currentDate.format(formatter);
            // check if already reviewed
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+rev.getAuthor()+"'}), (n2 {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED {date: '"+ formattedDate +"', title: '"+rev.getTitle()+"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkLike(User usr, Game game){
        try(Session session= Neo4jDbManager.getDriver().session()){
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d yyyy");
            String formattedDate = currentDate.format(formatter);
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ formattedDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static boolean checkIfAlreadyFollowed(User follower, User followed){
        try (Session session =  Neo4jDbManager.getDriver().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
                    "RETURN COUNT(*) > 0 as followExists");
            return result.single().get("followExists").asBoolean();
        }
    }

    public void suggestTrendingNowAmongFollowed(User usr){
        // prendi dal grafo la lista di amici
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // creiamo una struttura hashmap con keys: tutti i giochi a cui gli amici hanno messo like
        HashMap<String, Float> mapScores = new HashMap<>();
        // vedi qual è il like più vecchio messo e salva di quanti giorni è vecchio (es. int firstLike = 153)
        // ora scorriamo ogni amico e aggiungiamo i pesi dei giochi a cui lui ha messo like:
        //      il peso aggiornato sarà: pesoAttuale+nuovoPeso
        //      dove nuovoPeso = e^[(firstLike-daysLike)/firstLike] (daysLike sarebbe da quanti giorni è stato messo il like che stiamo guardando ora)
        // finito di aggiornare tutti i pesi ritorni il titolo del gioco con il peso maggiore
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

        Neo4jDbManager dbManager = new Neo4jDbManager();
        /*
        User usr1 = new User("a", "b", "d_rowe2583");
        User usr2 = new User("c", "d","m_linda3865");
        User usr3 = new User("e", "f","pluto");
        addUserNode(usr1);
        addUserNode(usr2);
        addUserNode(usr3);
        addDirectedLinkFollow(usr2, usr1);
        boolean bool1 = checkIfAlreadyFollowed(usr2, usr1);
        boolean bool2 = checkIfAlreadyFollowed(usr1, usr2);
        boolean bool3 = checkIfAlreadyFollowed(usr1, usr3);
        System.out.println(bool1);
        System.out.println(bool2);
        System.out.println(bool3);


        User usr4 = new User("g", "h", "niko_pandetta");
        addUserNode(usr4);

        addDirectedLinkLike(usr4, new Game("among us", "bello bellissimo"));
        */
        User usr4 = new User("g", "h", "niko_pandetta");
        User usr5 = new User("c", "d", "m_linda3865");
        //addDirectedLinkFollow(usr5, usr4);
        /*
        ArrayList<User> lista = getListFollowedUsers(usr5);
        for (User usr: lista){
            System.out.println(usr.getFirstName() + " " + usr.getLastName() + " " + usr.getNick());
        }
        */
        /*
        LocalDate data = LocalDate.now();
        Review review = new Review(data, "niko_pandetta", "among us", "aaaaaaaa");
        addDirectedLinkReviewed(review);
        */
        Date data = new Date();
        Game pippo = new Game("paperino", "developer", data, 50);
        //GameManagerNeo4j.addGameNode(pippo);
        //addDirectedLinkLike(usr4, pippo);
        ArrayList<Game> list = getListLikedGames(usr4);
        for (Game game:list){
            System.out.println(game.getName());
        }
    }


}




