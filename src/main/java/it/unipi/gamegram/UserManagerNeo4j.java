package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Record;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class UserManagerNeo4j {

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

    // auxiliary method used to retrieve the name of a liked game and the date of the like
    private static ArrayList<Like> getLikedGameDated(User usr){
        ArrayList<Like> listLikes = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.username = '" + usr.getNick() +
                        "' RETURN liked.name, like.date");
                while (result.hasNext()) {
                    Record r = result.next();
                    String dateLikeString = r.get("like.date").asString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateLike = LocalDate.parse(dateLikeString, formatter);
                    // convert the date of the like in a long representing the day passed since the like
                    long dayPassed = ChronoUnit.DAYS.between(dateLike, today);
                    listLikes.add(new Like(r.get("liked.name").asString(), dayPassed));
                }
                return listLikes;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikes;
    }


    // method to create a user node in neo4j
    public static void addUserNode(User usr){
        try(Session session= Neo4jDriver.getInstance().session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run ("CREATE (:User { username:'" + usr.getNick() + "', lastname:'" + usr.getLastName() + "', firstname:'" + usr.getFirstName() + "'})");
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
            LocalDate currentDate = LocalDate.now();
            // both name of the author and game of reference are taken from the Review fields
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+rev.getAuthor()+"'}), (n2 {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED {date: '"+ currentDate +"', title: '"+rev.getTitle()+"'}]->(n2)");
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
    public static boolean cancelReview(User usr, Game game){
        try (Session session =  Neo4jDriver.getInstance().session()) {
            Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[review:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
                    "DELETE review");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // method to suggest to a user a new person to follow based on user already followed
    public static ArrayList<String> suggestWhoToFollow(User usr){
        // take the list of already followed users
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // larger list that will contain all the users followed by all the users that usr is following
        ArrayList<User> totalFollowedByFollowed = new ArrayList<>();
        // hashmap that will be used to assign a score to each potential suggested user
        HashMap<String, Integer> mapUsers = new HashMap<>();
        // for each followed user take the list of followed users
        for(User tmp : listFollowed)
            // add all those users to the larger list
            totalFollowedByFollowed.addAll(getListFollowedUsers(tmp));
        // put all those candidates in the hashmap
        for (User tmp : totalFollowedByFollowed){
            // the first time an user appears he gets a score equal to 1
            if(!mapUsers.containsKey(tmp.getNick())){
                mapUsers.put(tmp.getNick(), 1);
            }
            // if the user is already in the hashmap increase his score by 1
            // this means that is followed by more than one user already followed
            else{
                int oldValue = mapUsers.get(tmp.getNick());
                oldValue++;
                mapUsers.remove(tmp.getNick());
                mapUsers.put(tmp.getNick(), oldValue);
            }
        }
        // remove from the candidates all the users already followed and yourself
        for(User alreadyFollowed : listFollowed)
            mapUsers.remove(alreadyFollowed.getNick());
        mapUsers.remove(usr.getNick());
        // sort the entries of the hashmap in descending order of value
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(mapUsers.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        // retrieve the top 5 users with higher score
        ArrayList<String> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            top5.add(entries.get(0).getKey());
            entries.remove(0);
        }

        return top5;
    }


    // auxiliary method that taken the list of followed users return the oldest like
    // is exploited to calculate the score in the suggestTrendingNowAmongFollowed method
    private static long getOldestLikeAmongFollowed(User usr){
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        long maximumDayPassed = 0;
        // get the list of followed users
        for (User tmp:listFollowed){
            // get the list of likes of the followed users with the dates of the likes
            // and put all of them in a list
            ArrayList<Like> listLikes = getLikedGameDated(tmp);
            // find the oldest like in the list
            for(Like like:listLikes){
                if (like.dayPassedSinceLike > maximumDayPassed)
                    maximumDayPassed = like.dayPassedSinceLike;
            }
        }
        return maximumDayPassed;
    }

    // auxiliary method to compute the score to assign to a like based on how old it is.
    // is exploited in suggestTrendingNowAmongFollowed method
    private static double calculateScoreLike(long maximumDayPassed, long daysSinceLike){
        return Math.pow(2,(maximumDayPassed - daysSinceLike)/1000 );
    }

    // method to suggest a game that an user should like based on likes of people the user follows.
    // this method assigns different score based on how many users followed liked a game and on how
    // old those likes are. The score tends to reward games that are receiving a lot of likes in last days.
    public static ArrayList<String> suggestTrendingNowAmongFollowed(User usr){
        // get the list of followed users
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // create an hashmap with:
        // keys: all the games a followed user liked
        // value: the score associated with that game
        HashMap<String, Double> mapScores = new HashMap<>();
        // retrieve how old is the oldest like
        long maximumDayPassed = getOldestLikeAmongFollowed(usr);
        // retrieve all the likes of followed users with corresponding date
        ArrayList<Like> listLikes = new ArrayList<>();
        for (User tmp:listFollowed){
            listLikes.addAll(getLikedGameDated(tmp));
        }
        // for each like update the partial score assigned to the game based on how old the like is
        for (Like like:listLikes){
            // if is the first time the game appears just compute and assign the score
            if(!mapScores.containsKey(like.nameOfTheGame))
                mapScores.put(like.nameOfTheGame, calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
            // if the game already appeared compute the score of this like and increment the old value
            else{
                double oldScore = mapScores.get(like.nameOfTheGame);
                mapScores.replace(like.nameOfTheGame, oldScore, oldScore + calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
            }
        }
        // remove from the candidates game for the suggestion all the games the user already likes:
        // get the list of games the user already likes
        ArrayList<Game> listLiked = getListLikedGames(usr);
        // remove from the hashmap all the games already liked by the user
        for(Game alreadyLiked : listLiked){
            if(mapScores.containsKey(alreadyLiked.getName()))
                mapScores.remove(alreadyLiked.getName());
        }
        ArrayList<Map.Entry<String, Double>> entries = new ArrayList<>(mapScores.entrySet());
        // sort the hashmap in descending order of value
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        // retrieve the games corresponding to the top 5 highest values
        ArrayList<String> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            top5.add(entries.get(0).getKey());
            entries.remove(0);
        }
        // return the list of the top 5 games
        return top5;
    }

    // auxialiary class exploited in suggestTrendingNowAmongFollowed method
    private static class Like{
        String nameOfTheGame;
        long dayPassedSinceLike;

        private Like(String nameOfTheGame, long dayPassedSinceLike){
            this.nameOfTheGame = nameOfTheGame;
            this.dayPassedSinceLike = dayPassedSinceLike;
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

        User usr1 = new User("a", "a", "a");
        User usr2 = new User("b", "b","b");
        User usr3 = new User("c", "c","c");
        User usr4 = new User("d", "d", "d");
        User usr5 = new User("e", "e", "e");
        User usr6 = new User("f", "f", "f");
        User usr7 = new User("g", "g", "g");
        User usr8 = new User("h", "h", "h");
        User usr9 = new User("i", "i", "i");
        User usr10 = new User("l", "l", "l");
        User usr11 = new User("m", "m", "m");
        User usr12 = new User("n", "n", "n");

        addUserNode(usr1);
        addUserNode(usr2);
        addUserNode(usr3);
        addUserNode(usr4);
        addUserNode(usr5);
        addUserNode(usr6);
        addUserNode(usr7);
        addUserNode(usr8);
        addUserNode(usr9);
        addUserNode(usr10);
        addUserNode(usr11);
        addUserNode(usr12);


        addDirectedLinkFollow(usr1, usr2);
        addDirectedLinkFollow(usr1, usr3);
        addDirectedLinkFollow(usr1, usr4);
        addDirectedLinkFollow(usr1, usr5);
        addDirectedLinkFollow(usr1, usr6);
        addDirectedLinkFollow(usr1,usr7);
        addDirectedLinkFollow(usr1, usr8);
        addDirectedLinkFollow(usr1, usr3);
        addDirectedLinkFollow(usr1, usr9);
        addDirectedLinkFollow(usr1, usr10);
        addDirectedLinkFollow(usr1, usr11);
        addDirectedLinkFollow(usr1, usr12);




        Game a = new Game("a");
        Game b = new Game("b");
        Game c = new Game("c");
        Game d = new Game("d");
        Game e = new Game("e");
        Game f = new Game("f");
        Game g = new Game("g");
        Game h = new Game("h");
        Game i = new Game("i");
        Game j = new Game("j");
        Game k = new Game("k");

        GameManagerNeo4j.addGameNode(a);
        GameManagerNeo4j.addGameNode(b);
        GameManagerNeo4j.addGameNode(c);
        GameManagerNeo4j.addGameNode(d);
        GameManagerNeo4j.addGameNode(e);
        GameManagerNeo4j.addGameNode(f);
        GameManagerNeo4j.addGameNode(g);
        GameManagerNeo4j.addGameNode(h);
        GameManagerNeo4j.addGameNode(i);
        GameManagerNeo4j.addGameNode(j);
        GameManagerNeo4j.addGameNode(k);

        addDirectedLinkLike(usr5, a);
        addDirectedLinkLike(usr2, a);



        LocalDate d1 = LocalDate.parse("2014-10-10");
        LocalDate d2 = LocalDate.parse("2022-10-19");
        LocalDate d3 = LocalDate.parse("2022-10-12");
        LocalDate d4 = LocalDate.parse("2022-10-13");
        LocalDate d5 = LocalDate.parse("2019-10-14");
        LocalDate d6 = LocalDate.parse("2022-10-15");
        LocalDate d7 = LocalDate.parse("2022-10-16");
        LocalDate d8 = LocalDate.parse("2022-10-11");
        LocalDate d9 = LocalDate.parse("2023-01-01");
        LocalDate d10 = LocalDate.parse("2022-10-19");

        addDirectedLinkLikeWithDateByHand(usr2, j, d1);
        addDirectedLinkLikeWithDateByHand(usr3, b, d2);
        addDirectedLinkLikeWithDateByHand(usr4, c, d3);
        addDirectedLinkLikeWithDateByHand(usr5, d, d4);
        addDirectedLinkLikeWithDateByHand(usr6, e, d5);
        addDirectedLinkLikeWithDateByHand(usr7, f, d6);
        addDirectedLinkLikeWithDateByHand(usr8, g, d7);
        addDirectedLinkLikeWithDateByHand(usr9, h, d8);
        addDirectedLinkLikeWithDateByHand(usr10, i, d9);
        addDirectedLinkLikeWithDateByHand(usr11, a, d10);

       // String reviewText, LocalDate reviewDate, String author, String gameOfReference, String title;


        Review rev = new Review("aaaaa", LocalDate.parse("2022-11-11"), "a", "a", "agg");
        ReviewManagerNeo4j.addReviewDirectedEdge(rev);

        System.out.println(GameManagerNeo4j.countReviews("a"));





    }


}




