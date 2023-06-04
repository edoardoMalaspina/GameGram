package it.unipi.gamegram.utility;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.ReviewManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PopulatingGraph {

    // method to add a given list of users to neo4j graph
    public static void addListOfUsers(String pathOfList){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(pathOfList));
            // first line is skipped in order to skip the header of .csv files
            String usr = reader.readLine();
            while(usr != null){
                // read the list of users line by line
                usr = reader.readLine();
                if(usr != null) {
                    // get the nick of the user
                    String nick = usr.split(",")[4];
                    // add a user node to the graph
                    UserManagerNeo4j.addUserNode(nick);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to add a given list of game to the neo4j graph
    public static void addListOfGames(String pathOfList){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(pathOfList));
            // first line is skipped in order to skip the header of .csv files
            String game = reader.readLine();
            while(game!=null){
                game = reader.readLine();
                if (game != null && game.split(",").length>1) {
                    // get the name of the game
                    String name = game.split(",")[0];
                    Game newGame = new Game(name);
                    // add the game node to the graph
                    GameManagerNeo4j.addGameNode(newGame);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // method to add Reviewed relationship from a given list to neo4j graph
    public static void addListReview(String pathOfList){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(pathOfList));
            // first line is skipped in order to skip the header of .csv files
            String riga = reader.readLine();
            while(riga != null){
                riga = reader.readLine();
                if(riga != null) {
                    // get the author of the review
                    String author = riga.split(",")[4];
                    // get the game of reference
                    String gameOfReference = riga.split(",")[0];
                    Review newReview = new Review(author, gameOfReference);
                    // add Reviewed relationship to the graph
                    ReviewManagerNeo4j.addReviewLink(newReview);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args){
        PopulatingGraph.addListOfUsers("users.csv");
        addListOfGames("games.csv");
        addListReview("reviewWithAuthor.csv");
    }

}
