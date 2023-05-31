package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersMongoDB.ReviewManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.ReviewManagerNeo4j;
import it.unipi.gamegram.singletons.ReviewSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ReviewPageController {

    @FXML
    private Label title;

    @FXML
    private Label game;

    @FXML
    private Label author;

    @FXML
    private Label date;



    @FXML
    private Label reviewText;

    @FXML
    private Button back;

    @FXML
    private Button delete;

    public void initialize() {
        delete.setVisible(false);
        delete.setDisable(true);
        if(LoggedUser.getIsAdmin() || LoggedUser.getLoggedUser().getNick().equals(ReviewSingleton.getReview().getAuthor())) {
            delete.setVisible(true);
            delete.setDisable(false);
        }
        Review review = ReviewSingleton.getReview();
        String titleFront = review.getTitle();
        title.setText(titleFront);
        game.setText("Game: " + review.getGameOfReference());
        author.setText("Author: " + review.getAuthor());
        date.setText("Date: " + review.getReviewDate());
        reviewText.setText(titleFront + ": " + review.getReviewText());

    }

    @FXML
    private void delete() throws IOException {
        ReviewManagerMongoDB.deleteReview(ReviewSingleton.getReview().getGameOfReference(), ReviewSingleton.getReview().getAuthor());
        ReviewManagerNeo4j.cancelReview(ReviewSingleton.getReview().getAuthor(),ReviewSingleton.getReview().getGameOfReference());
        ReviewSingleton.setNull();
        if (ReviewSingleton.getFlag())
            GameGramApplication.setRoot("showgamereviews");
        else
            GameGramApplication.setRoot("showuserreviews");
    }

    @FXML
    private void back() throws IOException {
        if(ReviewSingleton.getFlag())
            GameGramApplication.setRoot("showgamereviews");
        else
            GameGramApplication.setRoot("showuserreviews");
    }

}
