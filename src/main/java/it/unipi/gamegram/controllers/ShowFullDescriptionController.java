package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import it.unipi.gamegram.singletons.GameSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ShowFullDescriptionController {

    @FXML
    private Label title;

    @FXML
    private Label fullDescription;

    @FXML
    private Button back;


    public void initialize() {
        title.setText(GameSingleton.getName() + "'s Full Description");
        fullDescription.setText((new Game(GameManagerMongoDB.findGameByName(GameSingleton.getName())).getFullDescription()));
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("gamepage");
    }

}
