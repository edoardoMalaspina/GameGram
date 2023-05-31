package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button back;

    @FXML
    private Button login;

    @FXML
    private TextField signInNick;

    @FXML
    private PasswordField signInPassword;

    @FXML
    private Label errorMessage;

    public void initialize() {
        LoggedUser user = LoggedUser.getInstance();
        errorMessage.setVisible(false);
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("start");
    }
    @FXML
    private void login() throws IOException {

        LoggedUser user;
        String nick;
        String password;
        Parent root;
        Stage stage;

        nick = signInNick.getText();
        password = signInPassword.getText();

        if (nick.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Nick or Password is missing.");
            errorMessage.setVisible(true);
            return;
        } else if (UserManagerMongoDB.checkCredentials(nick, password)) {
            errorMessage.setText("Wrong nickname or password.");
            errorMessage.setVisible(true);
            return;
        }

        user = LoggedUser.getInstance();
        user.setLoggedUser(nick);
        GameGramApplication.setRoot("userhome");

    }
}

