package com.example.gamegram;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

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

    public boolean followUser(User usrFrom, User usrTo){
        if(checkIfFollowed(usrFrom, usrTo)) // you are already following this user
            return false;
        else{
            try(Session session= neo4jDBM.getDriver().session()){

                session.writeTransaction((TransactionWork<Void>) tx -> {
                    tx.run ("CREATE (" + usrFrom.getUsername() +
                            ")-[:FOLLOW {date: dataDelFollow}]->" +
                            "(" + usrTo.getUsername() +")");
                    return null;
                } );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    // questa ci sta sia un porcaio
    public boolean checkIfFollowed(User usrFrom, User usrTo){
        try(Session session= neo4jDBM.getDriver().session()){
            Boolean bool;
            bool = session.readTransaction(tx -> {
                Result result = tx.run("MATCH (" + usrFrom.getUsername() + ":User)" +
                        " RETURN exists((" + usrFrom.getUsername() + ")-[:FOLLOW]->(" + usrTo.getUsername() + "))");
                if(result.peek().containsKey("true"))
                    return true;
                else
                    return false;
            });
            return  bool;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }


}




