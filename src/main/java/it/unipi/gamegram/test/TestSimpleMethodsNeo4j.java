package it.unipi.gamegram.test;

import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestSimpleMethodsNeo4j {

    public static void main(String args[]) {
        // create user for testing
        User usr1 = new User("user1");
        User usr2 = new User("user2");
        User usr3 = new User("user3");
        // create games for testing
        Game game1 = new Game("game1");
        Game game2 = new Game("game2");

        // add user nodes
        UserManagerNeo4j.addUserNode(usr1.getNick());
        UserManagerNeo4j.addUserNode(usr2.getNick());
        UserManagerNeo4j.addUserNode(usr3.getNick());
        //add game nodes
        GameManagerNeo4j.addGameNode(game1);
        GameManagerNeo4j.addGameNode(game2);

        // create follow relationships
        UserManagerNeo4j.follow(usr1, usr2);
        UserManagerNeo4j.follow(usr1, usr3);
        //create like relationships
        UserManagerNeo4j.like(usr1, game1);
        UserManagerNeo4j.like(usr1, game2);

        // test like method
        if (!UserManagerNeo4j.checkIfAlreadyLiked(usr1, game1) || !UserManagerNeo4j.checkIfAlreadyLiked(usr1, game2))
            System.out.println("Error in neo4j method like()");

        // test getListLikedGames method
        ArrayList<Game> listLiked = UserManagerNeo4j.getListLikedGames(usr1);
        for (Game game : listLiked) {
            if (!game.getName().equals(game1.getName()) && !game.getName().equals(game1.getName()))
                System.out.println("Error in neo4j method getListLikedGames()");
        }

        // delete like relationships
        UserManagerNeo4j.unlike(usr1, game1);
        UserManagerNeo4j.unlike(usr1, game2);

        // test method unlike
        if (UserManagerNeo4j.checkIfAlreadyLiked(usr1, game1) || UserManagerNeo4j.checkIfAlreadyLiked(usr1, game2))
            System.out.println("Error in neo4j method unlike()");

        // test again getListLikedGames method
        if (!UserManagerNeo4j.getListLikedGames(usr1).isEmpty())
            System.out.println("Error in neo4j method getListLikedGames()");

        // test fiollow method
        if (!UserManagerNeo4j.checkIfAlreadyFollowed(usr1, usr2) || !UserManagerNeo4j.checkIfAlreadyFollowed(usr1, usr3))
            System.out.println("Error in neo4j method follow()");

        // test getListFollowedUsers method
        ArrayList<User> listFollowed = UserManagerNeo4j.getListFollowedUsers(usr1);
        for (User usr : listFollowed) {
            if (!usr.getNick().equals(usr2.getNick()) && !usr.getNick().equals(usr3.getNick()))
                System.out.println("Error in neo4j method getListFollowedUsers()");
        }

        // delete user nodes added for testing
        UserManagerNeo4j.deleteUserNode(usr1.getNick());
        UserManagerNeo4j.deleteUserNode(usr2.getNick());
        UserManagerNeo4j.deleteUserNode(usr3.getNick());

        // delete game nodes added for testing
        GameManagerNeo4j.deleteGameNode(game1.getName());
        GameManagerNeo4j.deleteGameNode(game2.getName());

    }


}
