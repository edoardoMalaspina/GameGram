package it.unipi.gamegram.managersNeo4j;

import it.unipi.gamegram.drivers.Neo4jDriver;
import it.unipi.gamegram.entities.Review;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class ReviewManagerNeo4j {

    // method to delete Reviewed relationship in neo4j between an user and a game
    public static boolean cancelReview(String usr, String game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '"+ usr +"'})-[review:REVIEWED]->(n2 {name: '"+ game +"'})" +
                    "DELETE review");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to create a Reviewed relationship in neo4j
    public static void addReviewLink(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            // both name of the author and game of reference are taken from the Review fields
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1:User {nick: '"+rev.getAuthor()+"'}), (n2:Game {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
