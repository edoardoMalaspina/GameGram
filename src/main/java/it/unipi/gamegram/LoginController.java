package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import it.unipi.gamegram.Entities.User;

public class LoginController {

    @FXML
    private Button back;

    @FXML
    private Button login;

    @FXML
    private TextField signInEmail;

    @FXML
    private PasswordField signInPassword;

    @FXML
    private TextField errorMessage;

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
        String email = null;
        String password = null;
        Parent root;
        Stage stage;

        email = signInEmail.getText();
        password = signInPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Email or Password is missing.");
            errorMessage.setVisible(true);
            return;
        } else if ((User.checkCredentials(email, password))) {
            errorMessage.setText("Wrong username or password.");
            errorMessage.setVisible(true);
            return;
        }

        user = LoggedUser.getInstance();
        user.setLoggedUser(email);

        if (User.isAdmin(email)) {
            root = FXMLLoader.load(getClass().getResource("adminhome.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource("userhome.fxml"));
            }

        stage = (Stage) login.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

