package it.unipi.gamegram.drivers;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDriver {

    private static Driver driver = null;

    private Neo4jDriver() {
        // personal new connection to test functions
        String uri = "neo4j://localhost:7687";
        String user = "neo4j";
        String password = "password";
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public static Driver getInstance() {
        if (driver == null) {
            Neo4jDriver neo4j = new Neo4jDriver();
        }
        return driver;
    }

}
