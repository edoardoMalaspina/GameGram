package it.unipi.gamegram.test;

import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;

import java.util.ArrayList;

public class TestNeo4j {

    private class TestBasicMethods{

        public static void main(String args[]) {
            User usr1 = new User("user1");
            User usr2 = new User("user2");
            User usr3 = new User("user3");
            Game game1 = new Game("game1");
            Game game2 = new Game("game2");

            UserManagerNeo4j.addUserNode(usr1.getNick());
            UserManagerNeo4j.addUserNode(usr2.getNick());
            UserManagerNeo4j.addUserNode(usr3.getNick());

            GameManagerNeo4j.addGameNode(game1);
            GameManagerNeo4j.addGameNode(game2);

            UserManagerNeo4j.follow(usr1, usr2);
            UserManagerNeo4j.follow(usr1, usr3);

            UserManagerNeo4j.like(usr1, game1);
            UserManagerNeo4j.like(usr1, game2);

            if (!UserManagerNeo4j.checkIfAlreadyLiked(usr1, game1) || !UserManagerNeo4j.checkIfAlreadyLiked(usr1, game2))
                System.out.println("Error in neo4j method like()");

            ArrayList<Game> listLiked = UserManagerNeo4j.getListLikedGames(usr1);
            for (Game game : listLiked) {
                if (!game.getName().equals(game1.getName()) && !game.getName().equals(game1.getName()))
                    System.out.println("Error in neo4j method getListLikedGames()");
            }

            UserManagerNeo4j.unlike(usr1, game1);
            UserManagerNeo4j.unlike(usr1, game2);

            if (UserManagerNeo4j.checkIfAlreadyLiked(usr1, game1) || UserManagerNeo4j.checkIfAlreadyLiked(usr1, game2))
                System.out.println("Error in neo4j method unlike()");

            if (!UserManagerNeo4j.getListLikedGames(usr1).isEmpty())
                System.out.println("Error in neo4j method getListLikedGames()");

            if (!UserManagerNeo4j.checkIfAlreadyFollowed(usr1, usr2) || !UserManagerNeo4j.checkIfAlreadyFollowed(usr1, usr3))
                System.out.println("Error in neo4j method follow()");

            ArrayList<User> listFollowed = UserManagerNeo4j.getListFollowedUsers(usr1);
            for (User usr : listFollowed) {
                if (!usr.getNick().equals(usr2.getNick()) && !usr.getNick().equals(usr3.getNick()))
                    System.out.println("Error in neo4j method getListFollowedUsers()");
            }
        }

        private class TestComplexMethods{

            User edo = new User("edo");
            User pie = new User("edo");
            User carlucc = new User("edo");
            User alen = new User("edo");
            User marco = new User("edo");
            User dome = new User("edo");
            User leo = new User("edo");
            User paolo = new User("edo");
            User aleb = new User("edo");
            User valerio = new User("edo");



        }





    }


}
