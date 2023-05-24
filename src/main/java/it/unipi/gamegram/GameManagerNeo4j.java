package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;


public class GameManagerNeo4j {
    //private final Neo4jDriver neo4jDBD;

    /*
    public GameManagerNeo4j() {
        this.neo4jDBD = Neo4jDriver.getInstance();
    }
     */

    public static void addGameNode(Game game){
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (:Game { name: '" + game.getName() + "', short_description: '" + game.getShortDescription() + "'})");
                return null;
            });
            Neo4jDriver.closeNeo4J();
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
