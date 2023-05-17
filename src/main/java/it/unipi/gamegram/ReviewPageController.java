package it.unipi.gamegram;

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
    private Label review;

    @FXML
    private Button upvote;

    @FXML
    private Button unvote;

    @FXML
    private Button back;

    public void initialize() {
        Review review = ReviewSingleton.getReview();
        String titleFront = review.getTitle();
        title.setText(titleFront);
        game.setText("Game: " + review.getGameOfReference());
        author.setText("Author: " + review.getAuthor());
        date.setText("Date: " + review.getReviewDate());
        date.setText(titleFront + ": " + review.getReviewText());
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("showreviews");
    }

}
