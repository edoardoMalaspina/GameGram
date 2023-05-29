package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
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
        fullDescription.setText((new Game(Game.findByName(GameSingleton.getName())).getFullDescription()));
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("gamepage");
    }

}
