package it.unipi.gamegram.utility;

import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PopulatingGraph {

    public static void addListOfUsers(String pathOfList) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathOfList));
            String usr = reader.readLine();
            while (usr != null) {
                usr = reader.readLine(); // questo sta all'inizio così al primo giro scartiamo l'header
                if (usr != null) {
                    String nick = usr.split(",")[4];
                    UserManagerNeo4j.addUserNode(nick);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addListOfGames(String pathOfList) {
        BufferedReader reader;
        int count = 0;
        try {
            reader = new BufferedReader(new FileReader(pathOfList));
            String game = reader.readLine();
            while (game != null) {
                game = reader.readLine();
                if (game != null && game.split(",").length > 1) {
                    String name = game.split(",")[0];
                    Game newGame = new Game(name);
                    GameManagerNeo4j.addGameNode(newGame);
                    count++;
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            System.out.println(count);
            e.printStackTrace();
        }
    }

    public static void addListReview(String pathOfList) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathOfList));
            String riga = reader.readLine();
            int count = 0;
            while (riga != null) {
                riga = reader.readLine(); // questo sta all'inizio così al primo giro scartiamo l'header
                if (riga != null) {
                    String author = riga.split(",")[4];
                    String gameOfReference = riga.split(",")[0];
                    Review newReview = new Review(author, gameOfReference);
                    UserManagerNeo4j.addDirectedLinkReviewed(newReview);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main di prova di Edo
    public static void main(String[] args) {
        PopulatingGraph.addListOfUsers("C:\\Users\\edoar\\Desktop\\datasetProgettoGiusti\\users.csv");
        addListOfGames("C:\\Users\\edoar\\Desktop\\datasetProgettoGiusti\\datasetFinalePulitoRemovedWhitespaces.csv");
        addListReview("C:\\Users\\edoar\\Desktop\\datasetProgettoGiusti\\reviewWithAuthor.csv");
    }

}