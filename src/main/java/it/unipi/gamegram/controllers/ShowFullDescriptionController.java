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
        // Set the title label to the game's name and add the "'s Full Description" suffix
        title.setText(GameSingleton.getName() + "'s Full Description");

        // Get the game's full description and set it in the fullDescription label
        String gameName = GameSingleton.getName();
        Game game = new Game(GameManagerMongoDB.findGameByName(gameName));
        fullDescription.setText(game.getFullDescription());
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("gamepage");
    }

}