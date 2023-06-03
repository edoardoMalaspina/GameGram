package it.unipi.gamegram.managersNeo4j;
import it.unipi.gamegram.drivers.Neo4jDriver;
import it.unipi.gamegram.entities.Game;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class GameManagerNeo4j {

    // method to delete a game node in the neo4j graph
    public static void deleteGameNode(String game){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n {name: '" + game + "'})" +
                        "DETACH DELETE n");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to add a node corresponding to a game in the neo4j graph
    public static void addGameNode(Game game){
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (:Game { name: '" + game.getName() + "'})");
                return null;
            });
        } catch (Exception e) {
            System.err.println("Failed to create game node: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
