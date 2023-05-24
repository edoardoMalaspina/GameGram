package it.unipi.gamegram;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDriver{

    private static Neo4jDriver neo4j = null;
    private static Driver driver = null;
   // private final String uri = "neo4j://localhost:7687"; // original database
    private String uri; // personal new connection to test functions
    private String user;
    private String password;



    private Neo4jDriver(){
        uri = "bolt://localhost:7687";
        user = "neo4j";
        password = "password";
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public static Driver getInstance() {
        if(driver == null) {
            neo4j = new Neo4jDriver();
        }
        return driver;
    }

    public static void closeNeo4J() throws Exception {
        try{
            driver.close();

        }catch (Exception e){
            System.out.println("Error occurred opening Neo4j connection");
            e.printStackTrace();
        }
    }

}
