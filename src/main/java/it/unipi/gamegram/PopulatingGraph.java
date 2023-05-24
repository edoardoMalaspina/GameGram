package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PopulatingGraph {

    public static void addListOfUsers(String pathOfList){
        BufferedReader reader;
       // Neo4jDriver dbManager = new Neo4jDriver();
        //UserManagerNeo4j usrManager = new UserManagerNeo4j(dbManager);
        try{
            reader = new BufferedReader(new FileReader(pathOfList));
            String usr = reader.readLine();
            while(usr != null){
                usr = reader.readLine(); // questo sta all'inizio così al primo giro scartiamo l'header
                if(usr != null) {
                    String name = usr.split(",")[1];
                    String surname = usr.split(",")[4];
                    String nick = usr.split(",")[2];
                    User newUser = new User(name, surname, nick);
                    UserManagerNeo4j.addUserNode(newUser);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addListOfGames(String pathOfList){
        BufferedReader reader;
       // Neo4jDriver dbManager = new Neo4jDriver();
        GameManagerNeo4j gameManager = new GameManagerNeo4j();
        int count = 0;
        try{
            reader = new BufferedReader(new FileReader(pathOfList));
            String game = reader.readLine();
            while(game!=null){
                game = reader.readLine(); // questo sta all'inizio così al primo giro scartiamo l'header
                if (game != null && game.split(",").length>1) {
                    String name = game.split(",")[0];
                    String shortDesc = game.split(",")[6];
                    Game newGame = new Game(name, shortDesc);
                    gameManager.addGameNode(newGame);
                    count++;
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            System.out.println(count);
            e.printStackTrace();
        }
    }




    // main di prova di Edo
    public static void main(String[] args){
        PopulatingGraph.addListOfUsers("C:\\Users\\edoar\\Desktop\\users.csv");
        addListOfGames("C:\\Users\\edoar\\Desktop\\datasetFinale.csv");

    }

}
