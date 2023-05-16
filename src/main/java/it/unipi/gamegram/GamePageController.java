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
    private Label dateOfPublication;

    @FXML
    private Label developer;

    @FXML
    private Label publisher;

    @FXML
    private Label price;

    @FXML
    private Label shortDescription;

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
        title.setText(name + "'s game page");
        Game game = new Game(Game.findByName(name));
        dateOfPublication.setText("Date of publication: " + game.getStringDateOfPublication());
        developer.setText("Developer: " + game.getDeveloper());
        publisher.setText("Publisher: " + game.getPublisher());
        price.setText("Price: " + game.getPrice());
        shortDescription.setText("Short description: " + game.getShortDescription());
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");}



}
