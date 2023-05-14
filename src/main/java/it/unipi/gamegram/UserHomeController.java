package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import it.unipi.gamegram.Entities.*;

import java.io.IOException;

public class UserHomeController {
    @FXML
    private Button findUser;

    @FXML
    private Button findGame;

    @FXML
    private TextField userNick;

    @FXML
    private TextField gameName;

    @FXML
    private Label errorMessage;

    public void initialize() {
        errorMessage.setVisible(false);
    }

    @FXML
    private void findUser() throws IOException {

        UserSingleton.setNull();
        String nick = userNick.getText();

        if (nick.isEmpty()) {
            errorMessage.setText("Nick is missing.");
            errorMessage.setVisible(true);
            return;
        }
        if(User.checkNick(nick)){
            errorMessage.setText("No such user.");
            errorMessage.setVisible(true);
            return;
        }
        UserSingleton user = UserSingleton.getInstance(nick);
        GameGramApplication.setRoot("userpage");
    }

    @FXML
    private void findGame() throws IOException {

        GameSingleton.setNull();
        String name = gameName.getText();

        if (name.isEmpty()) {
            errorMessage.setText("Name is missing.");
            errorMessage.setVisible(true);
            return;
        }
        if(Game.checkName(name)){
            errorMessage.setText("No such game.");
            errorMessage.setVisible(true);
            return;
        }
        GameSingleton game = GameSingleton.getInstance(name);
        GameGramApplication.setRoot("gamepage");
    }
}