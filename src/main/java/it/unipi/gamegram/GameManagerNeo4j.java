package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class GameManagerNeo4j {
    private final Neo4jDbManager neo4jDBM;

    public GameManagerNeo4j(Neo4jDbManager neo4jDBM) {
        this.neo4jDBM = neo4jDBM;
    }


    public void addGameNode(Game game){
        try (Session session = neo4jDBM.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (:Game { name: '" + game.getName() + "', short_description: '" + game.getShortDescription() + "'})");
                return null;
            });
        } catch (Exception e) {
            System.err.println("Failed to create game node: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
