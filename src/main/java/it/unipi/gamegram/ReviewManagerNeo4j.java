package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.Date;

public class ReviewManagerNeo4j {

    private final Neo4jDbManager neo4jDBM;

    public ReviewManagerNeo4j(Neo4jDbManager neo4jDBM) {
        this.neo4jDBM = neo4jDBM;
    }

    // da testare
    public static void addReviewDirectedEdge(Review rev){
        try(Session session= Neo4jDbManager.getDriver().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("CREATE (" + rev.getAuthor() + ")-[:REVIEWED {date: " + rev.getReviewDate() + ", title: " + rev.getTitle() + "}]->(" + rev.getGameOfReference() + ")");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public static void main(String args[]){

    }


}