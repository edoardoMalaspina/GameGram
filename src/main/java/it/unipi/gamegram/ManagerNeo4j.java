package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ManagerNeo4j {

    // method to retrieve from neo4j the list of users followed by usr
    public static ArrayList<User> getListFollowedUsers(User usr) {
        ArrayList<User> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(followed:User) " +
                        "WHERE u.username = '" + usr.getNick() +
                        "' RETURN followed.firstname, followed.lastname, followed.username");
                // add to the list to return all the record in the result set of the query
                while (result.hasNext()) {
                    Record r = result.next();
                    listFollowedUsers.add(new User( r.get("followed.firstname").asString(), r.get("followed.lastname").asString(), r.get("followed.username").asString()));
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
                        "WHERE u.username = '" + usr.getNick() +
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
                tx.run ("CREATE (:User { username:'" + usr + "'})");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteUserNode(String usr){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n {username: '" + usr + "'})" +
                        "DETACH DELETE n");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

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

    // method to create a Follow relationship between follower and followed in neo4j
    public static void addDirectedLinkFollow(User follower, User followed){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+follower.getNick()+"'}), (n2 {username: '"+followed.getNick()+"'})" +
                        "CREATE (n1)-[:FOLLOW {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to create a Reviewed relationship in neo4j
    public static void addDirectedLinkReviewed(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            // both name of the author and game of reference are taken from the Review fields
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1:User {username: '"+rev.getAuthor()+"'}), (n2:Game {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    // method to create a Like relationship between usr and game
    public static void addDirectedLinkLike(User usr, Game game){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //questa è solo per testare DA TOGLEIRE
    public static void addDirectedLinkLikeWithDateByHand(User usr, Game game, LocalDate dataScelta){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+usr.getNick()+"'}), (n2 {name: '"+game.getName()+"'})" +
                        "CREATE (n1)-[:LIKE {date: '"+ dataScelta +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // method to chech if an user is already following another user
    public static boolean checkIfAlreadyFollowed(User follower, User followed){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
                    "RETURN COUNT(*) > 0 as followExists");
            return result.single().get("followExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to check if an user already likes a game
    public static boolean checkIfAlreadyLiked(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[:LIKE]->(n2 {name: '"+ game.getName() +"'})" +
                    "RETURN COUNT(*) > 0 as likeExists");
            return result.single().get("likeExists").asBoolean();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to check if an user already wrote a review for a game
    private static boolean checkIfAlreadyReviewed(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
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
            Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[follow:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
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
            Result result = session.run("MATCH (n1 {username: '" + usr.getNick() + "'})-[like:LIKE]->(n2 {name: '" + game.getName() + "'})" +
                    "DELETE like");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // method to delete Reviewed relationship in neo4j between an user and a game
    public static boolean cancelReview(String usr, String game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr +"'})-[review:REVIEWED]->(n2 {name: '"+ game +"'})" +
                    "DELETE review");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> suggestWhoToFollow(User usr){
        ArrayList<String> top5 = new ArrayList<>();
        try (Session session =  Neo4jDriver.getInstance().session()) {
            String query = "MATCH (u1:User {username: '"+ usr.getNick() +"'})-[:FOLLOW]->(:User)-[:FOLLOW]->(u2:User) " +
                    "WHERE NOT EXISTS((u1)-[:FOLLOW]->(u2)) " +
                    "RETURN u2.username AS RecommendedUser, COUNT(*) AS Followers " +
                    "ORDER BY Followers DESC " +
                    "LIMIT 5";
            Result result = session.run(query);
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
            String query = "MATCH (u:User {username: '" + usr.getNick() + "'})-[:FOLLOW]->(followed:User) " +
                    "MATCH (followed)-[:LIKE|REVIEWED]->(game:Game) " +
                    "WITH followed, count(*) as totalEdges " +
                    "ORDER BY totalEdges DESC " +
                    "LIMIT 5 " +
                    "RETURN followed.username";
            Result result = session.run(query);
            for(Record r : result.list()){
                String recommendedUser = r.get("followed.username").asString();
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
            String query = "MATCH (p:User {username: '"+usr.getNick()+"'})-[:FOLLOW]->(u:User)-[l:LIKE]->(g:Game) " +
                    "WHERE NOT EXISTS((p)-[:LIKE]->(g)) " +
                    "WITH g, l.date AS likeDate, u " +
                    "ORDER BY likeDate DESC " +
                    "WITH g, COLLECT(u) AS likedBy, COLLECT(likeDate) AS likeDates " +
                    "WITH g, likedBy, REDUCE(score = 0.0, i IN RANGE(0, SIZE(likedBy)-1) | score+round((toFloat(datetime().epochSeconds-datetime(likeDates[i]).epochSeconds)/(3600 * 24))^2 * 100)/100) AS partialScore " +
                    "RETURN g.name, SUM(partialScore) AS totalScore " +
                    "ORDER BY totalScore ASC " +
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

    // method to count exploiting neo4j the number of likes a game received
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

    // method to count exploiting neo4j the number of reviews a game received
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


    // method to add in the neo4j graph a node corresponding to a game
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




