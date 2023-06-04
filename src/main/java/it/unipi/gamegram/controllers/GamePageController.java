package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import it.unipi.gamegram.managersMongoDB.ReviewManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.GameSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class GamePageController {

    @FXML
    private Label title;

    @FXML
    private Label dateOfPublication;

    @FXML
    private Label developer;

    @FXML
    private Label publisher;

    @FXML
    private Label price;

    @FXML
    private Label shortDescription;

    @FXML
    private Label outcomeMessage;

    @FXML
    private Button like;

    @FXML
    private Button unlike;

    @FXML
    private Button showReviews;

    @FXML
    private Button showFullDescription;

    @FXML
    private Button writeReview;

    @FXML
    private Button back;

    @FXML
    private Label numberOfLikes;

    @FXML
    private Label numberOfReviews;

    @FXML
    private Button delete;

    public void initialize() {
        delete.setVisible(false);
        delete.setDisable(true);
        if (LoggedUser.getIsAdmin()) {
            delete.setVisible(true);
            delete.setDisable(false);
        }
        String name = GameSingleton.getName();
        title.setText(name + "'s game page");
        Game game = new Game(GameManagerMongoDB.findGameByName(name));
        dateOfPublication.setText("Date of publication: " + game.getDateOfPublication());
        developer.setText("Developer: " + game.getDeveloper());
        publisher.setText("Publisher: " + game.getPublisher());
        price.setText("Price: " + game.getPrice());
        shortDescription.setText("Short description: " + game.getShortDescription());
        numberOfLikes.setText("Number of likes: " + UserManagerNeo4j.countLikes(GameSingleton.getName()));
        numberOfReviews.setText("Number of reviews: " + UserManagerNeo4j.countReviews(GameSingleton.getName()));
    }

    @FXML
    private void showReviews() throws IOException {
        GameGramApplication.setRoot("showgamereviews");
    }

    @FXML
    private void writeReview() throws IOException {
        if (ReviewManagerMongoDB.findReviewByGameAndAuthor(GameSingleton.getName(), LoggedUser.getLoggedUser().getNick())) {
            outcomeMessage.setText("You already wrote a review for this game.");
            return;
        }
        GameGramApplication.setRoot("writereview");
    }

    @FXML
    private void like() {
        Game game = new Game(GameSingleton.getName());
        if (UserManagerNeo4j.checkIfAlreadyLiked(LoggedUser.getLoggedUser(), game)) {
            outcomeMessage.setText("Game already liked.");
            return;
        }
        UserManagerNeo4j.like(LoggedUser.getLoggedUser(), game);
        numberOfLikes.setText("Number of likes: " + UserManagerNeo4j.countLikes(GameSingleton.getName()));
        outcomeMessage.setText("Game successfully liked.");
    }

    @FXML
    private void unlike() {
        Game game = new Game(GameSingleton.getName());
        if (!UserManagerNeo4j.checkIfAlreadyLiked(LoggedUser.getLoggedUser(), game)) {
            outcomeMessage.setText("Game not liked yet.");
            return;
        }
        UserManagerNeo4j.unlike(LoggedUser.getLoggedUser(), game);
        numberOfLikes.setText("Number of likes: " + UserManagerNeo4j.countLikes(GameSingleton.getName()));
        outcomeMessage.setText("Game successfully unliked.");
    }

    @FXML
    private void showFullDescription() throws IOException {
        GameGramApplication.setRoot("showfulldescription");
    }

    @FXML
    private void delete() throws IOException {
        // save a copy in case it has to be inserted again
        Game tmp = new Game(GameManagerMongoDB.findGameByName(GameSingleton.getName()));
        // flag to know which database has problem
        boolean flag = true;
        try {
            GameManagerMongoDB.deleteGame(GameSingleton.getName());
            // MongoDB's operation successfully completed
            flag = false;
            GameManagerNeo4j.deleteGameNode(GameSingleton.getName());
        } catch(Exception e){
            if(!flag) {
                // if enters here the database with a problem was Noe4j
                // add again the Game in MongoDB for consistency
                GameManagerMongoDB.insertGame(tmp.getName(), tmp.getDateOfPublication(), tmp.getDeveloper(),
                        tmp.getPublisher(), tmp.getPrice(), tmp.getShortDescription(), tmp.getFullDescription());
            }
            outcomeMessage.setText("Error while deleting game");
        }
        GameSingleton.setNull();
        GameGramApplication.setRoot("userhome");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }
}