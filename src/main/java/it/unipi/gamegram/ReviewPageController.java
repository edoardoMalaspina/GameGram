package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
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
    private Button upvote;

    @FXML
    private Button unvote;

    @FXML
    private Button back;

    @FXML
    private Button delete;

    public void initialize() {
        delete.setVisible(false);
        delete.setDisable(true);
        if(LoggedUser.getIsAdmin()) {
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
        Review.delete(ReviewSingleton.getReview().getGameOfReference(), ReviewSingleton.getReview().getAuthor());
        ReviewSingleton.setNull();
        GameGramApplication.setRoot("gamepage");}

    @FXML
    private void back() throws IOException {
        if(ReviewSingleton.getFlag())
            GameGramApplication.setRoot("showgamereviews");
        else
            GameGramApplication.setRoot("showuserreviews");
    }

}
