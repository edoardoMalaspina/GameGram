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

    public static ArrayList<User> getListFollowedUsers(User usr) {
        ArrayList<User> listFollowedUsers = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[:FOLLOW]->(followed:User) " +
                        "WHERE u.username = '" + usr.getNick() +
                        "' RETURN followed.firstname, followed.lastname, followed.username");
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

    public static ArrayList<Game> getListLikedGames(User usr){
        ArrayList<Game> listLikedGames = new ArrayList<>();
        try (Session session = Neo4jDriver.getInstance().session()) {
            session.readTransaction(tx -> {
                Result result = tx.run("MATCH (u:User)-[like:LIKE]->(liked:Game) " +
                        "WHERE u.username = '" + usr.getNick() +
                        "' RETURN liked.name");
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
                    long dayPassed = ChronoUnit.DAYS.between(dateLike, today);
                    System.out.println("AAAAAAAAAAAAAAAAA" + dayPassed);
                    listLikes.add(new Like(r.get("liked.name").asString(), dayPassed));
                }
                return listLikes;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLikes;
    }


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

    public static void addDirectedLinkFollow(User follower, User followed){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            if(checkIfAlreadyFollowed(follower, followed)){
                System.out.println("You are already following this user"); //vedere come sistemare con l'interfaccia grafica
                return;
            }
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+follower.getNick()+"'}), (n2 {username: '"+followed.getNick()+"'})" +
                        "CREATE (n1)-[:FOLLOW {date: '"+ currentDate +"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addDirectedLinkReviewed(Review rev){
        try(Session session= Neo4jDriver.getInstance().session()){
            LocalDate currentDate = LocalDate.now();
            // check if already reviewed
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n1 {username: '"+rev.getAuthor()+"'}), (n2 {name: '"+rev.getGameOfReference()+"'})" +
                        "CREATE (n1)-[:REVIEWED {date: '"+ currentDate +"', title: '"+rev.getTitle()+"'}]->(n2)");
                return null;
            } );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

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


    public static boolean unfollow(User follower, User followed){
        if ( !checkIfAlreadyFollowed(follower, followed) ){
            System.out.println( "You are not following this user" );
            return false;
        }
        else {
            try (Session session =  Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '"+ follower.getNick() +"'})-[follow:FOLLOW]->(n2 {username: '"+ followed.getNick() +"'})" +
                        "DELETE follow");
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean unlike(User usr, Game game){

        if ( !checkIfAlreadyLiked(usr, game) ){
            System.out.println( "You are not liking this game" );
            return false;
        }
        else {
            try (Session session = Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '" + usr.getNick() + "'})-[like:LIKE]->(n2 {name: '" + game.getName() + "'})" +
                        "DELETE like");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static boolean cancelReview(User usr, Game game){
        if( !checkIfAlreadyReviewed(usr, game) ){
            System.out.println( "You have not written a review for this game" );
            return false;
        }
        else{
            try (Session session =  Neo4jDriver.getInstance().session()) {
                Result result = session.run("MATCH (n1 {username: '"+ usr.getNick() +"'})-[review:REVIEWED]->(n2 {name: '"+ game.getName() +"'})" +
                        "DELETE review");
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public static ArrayList<String> suggestWhoToFollow(User usr){
        // prendo la lista di persone che seguo
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        ArrayList<User> totalFollowedByFollowed = new ArrayList<>();
        HashMap<String, Integer> mapUsers = new HashMap<>();
        // per ogni utente della lista che seguo metto in una lista gli utenti che segue
        for(User tmp : listFollowed)
            totalFollowedByFollowed.addAll(getListFollowedUsers(tmp));
        // togliamo dalla lista totale gli utenti che seguo già e me stesso
        for(User alreadyFollowed : listFollowed)
            totalFollowedByFollowed.remove(alreadyFollowed);
        totalFollowedByFollowed.remove(usr);
        // metto in un'hashmap tutti questi candidati e alzo il punteggio di uno ogni volta che si ripetono
        for (User tmp : totalFollowedByFollowed){
            if(!mapUsers.containsKey(tmp.getNick())){
                mapUsers.put(tmp.getNick(), 1);
            }
            else{
                int oldValue = mapUsers.get(tmp.getNick());
                oldValue++;
                mapUsers.remove(tmp.getNick());
                mapUsers.put(tmp.getNick(), oldValue);
            }
        }
        // Sort the entries by value in descending order
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(mapUsers.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        // Retrieve the keys corresponding to the highest values
        ArrayList<String> keys = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            keys.add(entries.get(0).getKey());
            entries.remove(0);
        }
        return keys;
    }


    // method that taken the list of followed users return the oldest like
    // da testare
    private static long getOldestLikeAmongFollowed(User usr){
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        long maximumDayPassed = 0;
        for (User tmp:listFollowed){
            ArrayList<Like> listLikes = getLikedGameDated(tmp);
            for(Like like:listLikes){
                if (like.dayPassedSinceLike > maximumDayPassed)
                    maximumDayPassed = like.dayPassedSinceLike;
            }
        }
        return maximumDayPassed;
    }

    // da testare
    private static double calculateScoreLike(long maximumDayPassed, long daysSinceLike){
        return Math.pow(2,(maximumDayPassed - daysSinceLike)/1000 );
    }

    // da testare   DA CCONTROLLAREEEEEE
    public static ArrayList<String> suggestTrendingNowAmongFollowed(User usr){
        // prendi dal grafo la lista di amici
        ArrayList<User> listFollowed = getListFollowedUsers(usr);
        // creiamo una struttura hashmap con keys: tutti i giochi a cui gli amici hanno messo like
        HashMap<String, Double> mapScores = new HashMap<>();
        long maximumDayPassed = getOldestLikeAmongFollowed(usr);
        ArrayList<Like> listLikes = new ArrayList<>();
        for (User tmp:listFollowed){
            listLikes.addAll(getLikedGameDated(tmp));
        }

        System.out.println("////////////////////////");
        for (Like like:listLikes){
            System.out.println(like.nameOfTheGame + " " + like.dayPassedSinceLike);
        }

        System.out.println("////////////////////////");





        for (Like like:listLikes){
            if(!mapScores.containsKey(like.nameOfTheGame))
                mapScores.put(like.nameOfTheGame, calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
            else{
                double oldScore = mapScores.get(like.nameOfTheGame);
                mapScores.replace(like.nameOfTheGame, oldScore, oldScore + calculateScoreLike(maximumDayPassed, like.dayPassedSinceLike));
            }
        }
        for (String str: mapScores.keySet())
            System.out.println(str + " " + +mapScores.get(str));
        // eliminiamo i giochi a cui ho già messo like:
        //prendo la lista dei giochi a cui ho già messo like
        ArrayList<Game> listLiked = getListLikedGames(usr);
        //tolgo dall'hashmap tutti i giochi a cui avevo già messo like
        for(Game alreadyLiked : listLiked){
            if(mapScores.containsKey(alreadyLiked.getName()))
                mapScores.remove(alreadyLiked.getName());
        }
        // ora vediamo quali sono i top 5 giochi con score più alto
        ArrayList<Map.Entry<String, Double>> entries = new ArrayList<>(mapScores.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        // Retrieve the keys corresponding to the highest values
        ArrayList<String> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            top5.add(entries.get(0).getKey());
            System.out.println(entries.get(0));
            entries.remove(0);

        }
        return top5;
    }

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

        ArrayList<String> listSuggestedGames = suggestTrendingNowAmongFollowed(usr1);
        for(String str:listSuggestedGames)
            System.out.println(str);





    }


}




