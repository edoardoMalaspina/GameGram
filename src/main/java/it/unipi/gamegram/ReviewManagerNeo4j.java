package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class ReviewManagerNeo4j {

    //private final Neo4jDriver neo4jDBM;

    /*
    public ReviewManagerNeo4j(Neo4jDriver neo4jDBM) {
        this.neo4jDBM = neo4jDBM;
    }
     */

    // da testare
    public static void addReviewDirectedEdge(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("MATCH (n1 {username: '"+rev.getAuthor()+"'}), (n2 {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED {date: '"+ rev.getReviewDate() +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }





    public static void main(String args[]){

    }


}
