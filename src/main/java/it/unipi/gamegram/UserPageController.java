package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UserPageController {

    @FXML
    private Label title;

    public void initialize() {
        String nick = UserSingleton.getNick();
        title.setText(nick + "'s User Page");
    }

}
