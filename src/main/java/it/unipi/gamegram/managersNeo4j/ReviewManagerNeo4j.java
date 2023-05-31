package it.unipi.gamegram.managersNeo4j;

import it.unipi.gamegram.drivers.Neo4jDriver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class ReviewManagerNeo4j {

    // method to delete Reviewed relationship in neo4j between an user and a game
    public static boolean cancelReview(String usr, String game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr +"'})-[review:REVIEWED]->(n2 {name: '"+ game +"'})" +
                    "DELETE review");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
