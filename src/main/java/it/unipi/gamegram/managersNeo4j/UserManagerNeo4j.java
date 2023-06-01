package it.unipi.gamegram.managersNeo4j;

import it.unipi.gamegram.drivers.Neo4jDriver;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.entities.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.time.LocalDate;
import java.util.*;

public class UserManagerNeo4j {

    // method to retrieve from neo4j the list of users followed by usr
    public static ArrayList<User> getListFollowedUsers(User usr) {
        ArrayList<User> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(followed:User) " +
                        "WHERE u.nick = '" + usr.getNick() +
                        "' RETURN followed.nick");
                // add to the list to return all the record in the result set of the query
                while (result.hasNext()) {
                    Record r = result.next();
                    listFollowedUsers.add(new User( r.get("followed.nick").asString()) );
                }
                return listFollowedUsers;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFollowedUsers;
    }

    // method to retrieve the list of games liked by usr
    public static ArrayList<Game> getListLikedGames(User usr){
        ArrayList<Game> listLikedGames = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.nick = '" + usr.getNick() +
                        "' RETURN liked.name");
                // add to the list to return all the record in the result set of the query
                while (result.hasNext()) {
                    Record r = result.next();
                    listLikedGames.add(new Game(r.get("liked.name").asString()));
                }
                return listLikedGames;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikedGames;
    }

    // method to create a user node in neo4j
    public static void addUserNode(String usr){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("CREATE (:User { nick:'" + usr + "'})");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to delete a user node in neo4j
    public static void deleteUserNode(String usr){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n {nick: '" + usr + "'})" +
                        "DETACH DELETE n");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to create a Follow relationship between follower and followed in neo4j
    public static void follow(User follower, User followed){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {nick: '"+follower.getNick()+"'}), (n2 {nick: '"+followed.getNick()+"'})" +
                        "CREATE (n1)-[:FOLLOW {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to create a Reviewed relationship in neo4j
    public static void addReviewLink(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            // both name of the author and game of reference are taken from the Review fields
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1:User {nick: '"+rev.getAuthor()+"'}), (n2:Game {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to create a Like relationship between usr and game
    public static void like(User usr, Game game){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {nick: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to create a like relationship in neo4j imposing the date of the like,
    // in the method like() the date is taken when user likes a game.
    // This method is not used in the app, is exploited just for testing purposes
    // in class TestComplexMethodsNeo4j to simulate different score for recent like and
    // old likes in recommendation system (suggestTrendingNowAmongFollowed  method)
    public static void addDirectedLinkLikeWithDateByHand(User usr, Game game, LocalDate dataScelta){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {nick: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ dataScelta +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to check if a user is already following another user
    public static boolean checkIfAlreadyFollowed(User follower, User followed){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '"+ follower.getNick() +"'})-[:FOLLOW]->(n2 {nick: '"+ followed.getNick() +"'})" +
                    "RETURN COUNT(*) > 0 as followExists");
            return result.single().get("followExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to check if a user already likes a game
    public static boolean checkIfAlreadyLiked(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '"+ usr.getNick() +"'})-[:LIKE]->(n2 {name: '"+ game.getName() +"'})" +
                    "RETURN COUNT(*) > 0 as likeExists");
            return result.single().get("likeExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to check if a user already wrote a review for a game
    private static boolean checkIfAlreadyReviewed(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '"+ usr.getNick() +"'})-[:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
                    "RETURN COUNT(*) > 0 as reviewExists");
            return result.single().get("reviewExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    // method to delete Follow relationship in neo4j between two users
    public static boolean unfollow(User follower, User followed){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '"+ follower.getNick() +"'})-[follow:FOLLOW]->(n2 {nick: '"+ followed.getNick() +"'})" +
                    "DELETE follow");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to delete Like relationship in neo4j between an user and a game
    public static boolean unlike(User usr, Game game){
        try (Session session = Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {nick: '" + usr.getNick() + "'})-[like:LIKE]->(n2 {name: '" + game.getName() + "'})" +
                    "DELETE like");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // method to suggest to a user five new users to follow considering the users already followed.
    public static ArrayList<String> suggestWhoToFollow(User usr){
        ArrayList<String> top5 = new ArrayList<>();
        // find the users more followed by users followed by usr
        try (Session session =  Neo4jDriver.getInstance().session()) {
            String query = "MATCH (u1:User {nick: '"+ usr.getNick() +"'})-[:FOLLOW]->(:User)-[:FOLLOW]->(u2:User) " +
                    "WHERE NOT EXISTS((u1)-[:FOLLOW]->(u2)) " +
                    "RETURN u2.nick AS RecommendedUser, COUNT(*) AS Followers " +
                    "ORDER BY Followers DESC " +
                    "LIMIT 5";
            Result result = session.run(query);
            // put the 5 returned users in a list
            while (result.hasNext()) {
                Record record = result.next();
                String recommendedUser = record.get("RecommendedUser").asString();
                top5.add(recommendedUser);
            }
        }
        return top5;
    }

    // method to find the top 5 active users among the followed.
    // return the five users that have liked or reviewed more games
    public static ArrayList<String> findMostActiveFollowed(User usr){
        ArrayList<String> top5 = new ArrayList<>();
        try (Session session =  Neo4jDriver.getInstance().session()) {
            String query = "MATCH (u:User {nick: '" + usr.getNick() + "'})-[:FOLLOW]->(followed:User) " +
                    "MATCH (followed)-[:LIKE|REVIEWED]->(game:Game) " +
                    "WITH followed, count(*) as totalEdges " +
                    "ORDER BY totalEdges DESC " +
                    "LIMIT 5 " +
                    "RETURN followed.nick";
            Result result = session.run(query);
            // put the five returned users in a lost
            for(Record r : result.list()){
                String recommendedUser = r.get("followed.nick").asString();
                top5.add(recommendedUser);
            }
        }
        return top5;
    }

    // method to suggest a game that a user should like based on likes of people the user follows.
    // this method assigns different score based on how many users followed liked a game and on how
    // old those likes are. The score tends to reward games that are receiving a lot of likes in last days.
    public static ArrayList<String> suggestTrendingNowAmongFollowed(User usr){
        ArrayList<String> top5 = new ArrayList<>();
        try (Session session =  Neo4jDriver.getInstance().session()) {
            String query = "MATCH (p:User {nick: '"+usr.getNick()+"'})-[:FOLLOW]->(u:User)-[l:LIKE]->(g:Game) " +
                    "WHERE NOT EXISTS((p)-[:LIKE]->(g)) " +
                    "WITH g, l.date AS likeDate, u " +
                    "ORDER BY likeDate DESC " +
                    "WITH g, COLLECT(u) AS likedBy, COLLECT(likeDate) AS likeDates " +
                    "WITH g, likedBy, likeDates, [i IN RANGE(0, SIZE(likedBy)-1) | CASE WHEN duration.inSeconds(datetime(), datetime(likeDates[i])).days > 0 THEN (1.0 / duration.inSeconds(datetime(), datetime(likeDates[i])).days) ELSE 0 END] AS partialScores " +
                    "WITH g, likedBy, REDUCE(score = 0.0, x IN partialScores | score + x) AS totalScore " +
                    "WITH g, totalScore, SIZE(likedBy) AS likedByCount " +
                    "RETURN g.name, totalScore, likedByCount " +
                    "ORDER BY likedByCount DESC, totalScore ASC " +
                    "LIMIT 5";
            Result result = session.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                String recommendedGame= record.get("g.name").asString();
                top5.add(recommendedGame);
            }
        }
        return top5;
    }

    // method to count the number of likes a game received exploiting neo4j
    public static int countLikes(String game) {
        try (Session session = Neo4jDriver.getInstance().session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run("MATCH (:User)-[:LIKE]->(game:Game) " +
                        "WHERE game.name = '" + game + "' " +
                        "RETURN COUNT(*) AS likeCount");
                Record record = result.next();
                return record.get("likeCount").asInt();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // method to count the number of reviews a game received exploiting neo4j
    public static int countReviews(String game) {
        try (Session session = Neo4jDriver.getInstance().session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run("MATCH (:User)-[:REVIEWED]->(game:Game) " +
                        "WHERE game.name = '" + game + "' " +
                        "RETURN COUNT(*) AS reviewCount");
                Record record = result.next();
                return record.get("reviewCount").asInt();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }





    public static void main(String[] args){

       // Neo4jDriver dbManager = new Neo4jDriver();
/*
        User usr1 = new User("a", "b", "d_rowe2583");
        User usr2 = new User("c", "d","m_linda3865");
        User usr3 = new User("e", "f","pluto");
        User usr4 = new User("g", "h", "niko_pandetta");
        User usr5 = new User("i", "l", "gennaro");


        addUserNode(usr1);
        addUserNode(usr2);
        addUserNode(usr3);
        addUserNode(usr4);
        addUserNode(usr5);

        LocalDate data = LocalDate.now();

        LocalDate dataa = LocalDate.now();
        Game pippo = new Game("paperino", "developer", dataa, 50);

        GameManagerNeo4j.addGameNode(pippo);
        addDirectedLinkLike(usr4, pippo);

        Review review = new Review(dataa, "niko_pandetta", "paperino", "aaaaaaaa");
        addDirectedLinkReviewed(review);

        addDirectedLinkFollow(usr4, usr3);
        addDirectedLinkFollow(usr4, usr1);
        addDirectedLinkFollow(usr2, usr1);
        addDirectedLinkFollow(usr5, usr2);
        addDirectedLinkFollow(usr5, usr4);

        /*
        User suggested = suggestFriend(usr5);
        System.out.println(suggested.getNick());
         */
        /*
        cancelReview(usr4, pippo);
        unfollow(usr4, usr3);
        */
        /*
        ArrayList<Game> list = getListLikedGames(usr4);
        for (Game game:list){
            System.out.println(game.getName());
        }

        ArrayList<Like> lista = getLikedGameDated(usr4);
        for (Like like:lista){
            System.out.println(like.nameOfTheGame + " " + like.dayPassedSinceLike);
        }

        unlike(usr4, pippo);


        User usr1 = new User("edoardo", "edoardo", "edoardo");
        User usr2 = new User("i", "l","gennaro");

        addUserNode(usr1);
        addUserNode(usr2);
        GameManagerNeo4j.addGameNode(pippo);
        GameManagerNeo4j.addGameNode(pluto);

        addDirectedLinkLike(usr2, pippo);
        addDirectedLinkLike(usr2, pluto);

        addDirectedLinkFollow(usr2, usr1);
        */


    }


}




