package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersMongoDB.ReviewManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.GameSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;

public class WriteReviewController {

    @FXML
    private Button back;

    @FXML
    private Button submit;

    @FXML
    private TextField reviewTitle;

    @FXML
    private TextArea reviewText;

    @FXML
    private Label outcomeMessage;

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("gamepage");
    }

    @FXML
    private void submit() {
        String text;
        String title;

        text = reviewText.getText();
        title = reviewTitle.getText();

        if (text.isEmpty() || title.isEmpty()) {
            outcomeMessage.setText("Fill all the fields.");
            return;
        }

        ReviewManagerMongoDB.insertReview(
                LoggedUser.getLoggedUser().getNick(),
                LocalDate.now(),
                title,
                text,
                GameSingleton.getName()
        );

        UserManagerNeo4j.addReviewLink(
                new Review(text, LocalDate.now(), LoggedUser.getLoggedUser().getNick(), GameSingleton.getName(), title)
        );

        outcomeMessage.setText("Review submitted. Go back to the game's page.");
        submit.setDisable(true);
    }

}