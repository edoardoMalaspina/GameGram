package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GamePageController {

    @FXML
    private Label title;

    @FXML
    private Label info;

    @FXML
    private Button like;

    @FXML
    private Button unlike;

    @FXML
    private Button showReviews;

    @FXML
    private Button back;


    public void initialize() {
        String name = GameSingleton.getName();
        title.setText(name + "'s Game Page");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");}



}
