package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private void submit() throws IOException {
        String text;
        String title;

        text = reviewText.getText();
        title = reviewTitle.getText();

        if(text.isEmpty() || title.isEmpty()){
            outcomeMessage.setText("Fill all the fields.");
            return;
        }
        Review.insert(LoggedUser.getLoggedUser().getNick(), LocalDate.now(), title, text, GameSingleton.getName());
        outcomeMessage.setText("Review submitted. Go back to game's page.");
        submit.setDisable(true);
    }

}
