package it.unipi.gamegram;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDbManager {

    public static Driver driver;
    private final String uri = "neo4j://localhost:7687"; // original database
   // private final String uri = "bolt://localhost:7687"; // personal new connection to test functions
    private final String user = "neo4j";
    private final String password = "admin";

    public Neo4jDbManager(){
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password)); //authentication without encryption
        }catch (Exception e){
            System.out.println("Error occurred opening Neo4j connection");
            e.printStackTrace();
        }
    }

    public void closeNeo4J() throws Exception {
        try{
            driver.close();
        }catch (Exception e){
            System.out.println("Error occurred opening Neo4j connection");
            e.printStackTrace();
        }
    }

    public static Driver getDriver() {
        return driver;
    }

}
