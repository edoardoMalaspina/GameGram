package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        Game game = new Game(Game.findByName(name));
        info.setText("date of publication: " + game.getStringDateOfPublication() + ","
                    + "developer: " + game.getDeveloper() + "," + "publisher: " + game.getPublisher() + "," + "price: "
                    + game.getPrice() + "," + "short description: " + game.getShortDescription() + ".");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");}



}
