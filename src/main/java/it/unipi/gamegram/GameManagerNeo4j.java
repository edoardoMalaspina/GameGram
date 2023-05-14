package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static it.unipi.gamegram.Neo4jDbManager.getDriver;

public class GameManagerNeo4j {
    private final Neo4jDbManager neo4jDBM;

    public GameManagerNeo4j(Neo4jDbManager neo4jDBM) {
        this.neo4jDBM = neo4jDBM;
    }


    public static void addGameNode(Game game){
        try (Session session = getDriver().session()) {
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
        Neo4jDbManager dbManager = new Neo4jDbManager();
        Game game1 = new Game("among us", "fanno ueueue faccio gnie gnie gnie");
        addGameNode(game1);
    }
}