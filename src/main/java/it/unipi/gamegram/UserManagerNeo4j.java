package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class UserManagerNeo4j {
    private final Neo4jDbManager neo4jDBM;

    public UserManagerNeo4j(Neo4jDbManager dbNeo4J) {
        this.neo4jDBM = dbNeo4J;
    }

    // da testare
    public ArrayList<String> getListFollowedUsers(User usr) {
        ArrayList<String> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDbManager.getDriver().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (username:User)-[:FOLLOW]-(followed) " +
                        "WHERE username.firstname = " + usr.getFirstName() +
                        "AND username.lastname = " + usr.getLastName() +
                        "AND username.username = " + usr.getNick() +
                        "RETURN followed");
                while (result.hasNext()) {
                    Record rec = (Record) result.next();
                    listFollowedUsers.add(rec.toString());
                }
                return listFollowedUsers;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFollowedUsers;
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
        */

        User usr4 = new User("g", "h", "niko_pandetta");
        addUserNode(usr4);

        addDirectedLinkLike(usr4, new Game("among us", "bello bellissimo"));
    }


}




