package it.unipi.gamegram.SceneControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

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
    private void back() throws IOException {
        GameGramApplication.setRoot("start");
    }

    private void login() throws IOException {
        //if bla bla
        GameGramApplication.setRoot("home");
    }

}