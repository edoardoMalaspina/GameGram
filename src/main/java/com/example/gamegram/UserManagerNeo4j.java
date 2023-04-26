package com.example.gamegram;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.ArrayList;
import java.util.HashSet;

public class UserManagerNeo4j {
    private final Neo4jDbManager neo4jDBM;


    public UserManagerNeo4j(Neo4jDbManager dbNeo4J) {
        this.neo4jDBM = dbNeo4J;
    }

    public ArrayList<String> getListFollowedUsers(User usr) {
        ArrayList<String> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDbManager.getDriver().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (username:User)-[:FOLLOW]-(followed) " +
                        "WHERE username.firstname = " + usr.getFirstName() +
                        "AND username.lastname = " + usr.getLastName() +
                        "AND username.username = " + usr.getUsername() +
                        "RETURN followed");
                while (result.hasNext()) {
                    Record rec = (Record) result.next();
                    listFollowedUsers.add(rec.toString());
                }
                return listFollowedUsers;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFollowedUsers;
    }


}

