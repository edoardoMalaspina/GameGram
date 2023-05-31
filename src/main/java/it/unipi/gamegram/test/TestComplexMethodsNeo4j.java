package it.unipi.gamegram.test;

import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestComplexMethodsNeo4j {

    public static void main(String args[]) {

        User edo = new User("edo");
        User pie = new User("pie");
        User carlucc = new User("carlucc");
        User alen = new User("alen");
        User marco = new User("marco");
        User dome = new User("dome");
        User leo = new User("leo");
        User paolo = new User("paolo");
        User aleb = new User("aleb");
        User valerio = new User("valerio");

        UserManagerNeo4j.addUserNode(edo.getNick());
        UserManagerNeo4j.addUserNode(pie.getNick());
        UserManagerNeo4j.addUserNode(carlucc.getNick());
        UserManagerNeo4j.addUserNode(alen.getNick());
        UserManagerNeo4j.addUserNode(marco.getNick());
        UserManagerNeo4j.addUserNode(dome.getNick());
        UserManagerNeo4j.addUserNode(leo.getNick());
        UserManagerNeo4j.addUserNode(paolo.getNick());
        UserManagerNeo4j.addUserNode(aleb.getNick());
        UserManagerNeo4j.addUserNode(valerio.getNick());

        Game marioKart = new Game("Mario Kart 27");
        Game sonic = new Game("Sonic 32");
        Game topolino = new Game("Topolino 12");
        Game fifa03 = new Game("FIFA 03");
        Game crash = new Game("Crash 82");
        Game lol = new Game("LeagueOfLegends");

        GameManagerNeo4j.addGameNode(marioKart);
        GameManagerNeo4j.addGameNode(sonic);
        GameManagerNeo4j.addGameNode(topolino);
        GameManagerNeo4j.addGameNode(fifa03);
        GameManagerNeo4j.addGameNode(crash);
        GameManagerNeo4j.addGameNode(lol);


        UserManagerNeo4j.follow(edo, marco);
        UserManagerNeo4j.follow(edo, pie);
        UserManagerNeo4j.follow(edo, carlucc);
        UserManagerNeo4j.follow(edo, alen);

        UserManagerNeo4j.follow(pie, marco);
        UserManagerNeo4j.follow(pie, dome);
        UserManagerNeo4j.follow(pie, leo);
        UserManagerNeo4j.follow(pie, paolo);

        UserManagerNeo4j.follow(carlucc, leo);
        UserManagerNeo4j.follow(carlucc, paolo);
        UserManagerNeo4j.follow(carlucc, aleb);
        UserManagerNeo4j.follow(carlucc, valerio);

        UserManagerNeo4j.follow(alen, paolo);
        UserManagerNeo4j.follow(alen, aleb);
        UserManagerNeo4j.follow(alen, valerio);


        UserManagerNeo4j.like(edo, marioKart);

        UserManagerNeo4j.like(pie, marioKart);
        UserManagerNeo4j.like(pie, sonic);

        UserManagerNeo4j.like(carlucc, sonic);
        UserManagerNeo4j.like(carlucc, topolino);
        UserManagerNeo4j.like(carlucc, fifa03);
        UserManagerNeo4j.like(carlucc, crash);
        UserManagerNeo4j.addDirectedLinkLikeWithDateByHand(carlucc, lol, LocalDate.parse("11 11 2012", DateTimeFormatter.ofPattern("dd MM yyyy")));

        UserManagerNeo4j.like(alen, sonic);
        UserManagerNeo4j.like(alen, topolino);

        ArrayList<String> whoToFollow = UserManagerNeo4j.suggestWhoToFollow(edo);
        System.out.println("Who to follow:");
        for(String str : whoToFollow){
            System.out.println(str);
        }

        System.out.println("\n");

        System.out.println("Trending now:");
        ArrayList<String> trendingNow = UserManagerNeo4j.suggestTrendingNowAmongFollowed(edo);
        for(String str : trendingNow){
            System.out.println(str);
        }

        System.out.println("\n");

        System.out.println("Most active followers:");
        ArrayList<String> mostActiveFollowers = UserManagerNeo4j.findMostActiveFollowed(edo);
        for(String str : mostActiveFollowers){
            System.out.println(str);
        }

        UserManagerNeo4j.deleteUserNode(edo.getNick());
        UserManagerNeo4j.deleteUserNode(pie.getNick());
        UserManagerNeo4j.deleteUserNode(carlucc.getNick());
        UserManagerNeo4j.deleteUserNode(alen.getNick());
        UserManagerNeo4j.deleteUserNode(marco.getNick());
        UserManagerNeo4j.deleteUserNode(dome.getNick());
        UserManagerNeo4j.deleteUserNode(leo.getNick());
        UserManagerNeo4j.deleteUserNode(paolo.getNick());
        UserManagerNeo4j.deleteUserNode(aleb.getNick());
        UserManagerNeo4j.deleteUserNode(valerio.getNick());

        GameManagerNeo4j.deleteGameNode(marioKart.getName());
        GameManagerNeo4j.deleteGameNode(sonic.getName());
        GameManagerNeo4j.deleteGameNode(topolino.getName());
        GameManagerNeo4j.deleteGameNode(fifa03.getName());
        GameManagerNeo4j.deleteGameNode(crash.getName());
        GameManagerNeo4j.deleteGameNode(lol.getName());

    }

}
