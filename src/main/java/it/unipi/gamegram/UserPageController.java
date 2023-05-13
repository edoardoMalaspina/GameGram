package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class UserPageController {

    @FXML
    private Label title;

    @FXML
    private Button follow;

    @FXML
    private Button unfollow;

    @FXML
    private Button showFollows;

    @FXML
    private Button showReviews;

    @FXML
    private Button back;

    public void initialize() {
        String nick = UserSingleton.getNick();
        title.setText(nick + "'s User Page");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
