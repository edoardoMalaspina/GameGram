package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        // Retrieve the values from the input fields
        nick = signInNick.getText();
        password = signInPassword.getText();

        // Check if the nick or password is missing
        if (nick.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Nick or Password is missing.");
            errorMessage.setVisible(true);
            return;
        }

        // Check if the credentials are valid
        if (UserManagerMongoDB.checkCredentials(nick, password)) {
            errorMessage.setText("Wrong nickname or password.");
            errorMessage.setVisible(true);
            return;
        }

        // Set the logged user and navigate to the user home page
        user = LoggedUser.getInstance();
        user.setLoggedUser(nick);
        GameGramApplication.setRoot("userhome");
    }
}