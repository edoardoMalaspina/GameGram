package it.unipi.gamegram.SceneControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartController {

    @FXML
    private Button login;

    @FXML
    private Button signup;

    @FXML
    private void login() throws IOException {
        GameGramApplication.setRoot("login");
    }

    private void signup() throws IOException {
        GameGramApplication.setRoot("signup");
    }
}
