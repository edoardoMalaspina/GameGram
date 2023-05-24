package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;


public class GameManagerNeo4j {

    public static int countLikes(String game) {

        try (Session session = Neo4jDriver.getInstance().session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run("MATCH (:User)-[:LIKE]->(game:Game) " +
                                "WHERE game.name = $gameName " +
                                "RETURN COUNT(*) AS likeCount",
                        Values.parameters("gameName", game));
                Record record = result.next();
                return record.get("likeCount").asInt();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }




    public static void addGameNode(Game game){
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (:Game { name: '" + game.getName() + "', short_description: '" + game.getShortDescription() + "'})");
                return null;
            });
        } catch (Exception e) {
            System.err.println("Failed to create game node: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
       // Neo4jDriver dbManager = new Neo4jDriver();
        Game game1 = new Game("among us", "fanno ueueue faccio gnie gnie gnie");
        addGameNode(game1);
    }
}



