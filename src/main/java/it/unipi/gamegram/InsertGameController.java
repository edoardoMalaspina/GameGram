package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class InsertGameController {

    @FXML
    private Button back;

    @FXML
    private Button insert;

    @FXML
    private TextField gameName;

    @FXML
    private DatePicker gameDateOfPublication;

    @FXML
    private TextField gameDeveloper;

    @FXML
    private TextField gamePublisher;

    @FXML
    private TextField gamePrice;

    @FXML
    private TextField gameShortDescription;

    @FXML
    private Label outcomeMessage;

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }
    @FXML
    private void insert() throws IOException {
        String name;
        LocalDate dateOfPublication;
        String developer;
        String publisher;
        double price;
        String shortDescription;


        name = gameName.getText();
        dateOfPublication = gameDateOfPublication.getValue();
        developer = gameDeveloper.getText();
        publisher = gamePublisher.getText();
        if(gamePrice.getText().isEmpty())
            price = 0;
        else
            price = Double.parseDouble(gamePrice.getText());
        shortDescription = gameShortDescription.getText();

        if(name.isEmpty()){
            outcomeMessage.setText("Insert at least the name.");
            return;
        }

        if(!Game.checkName(name)){
            outcomeMessage.setText("Game already exists.");
            return;
        }

        Game.insert(name, dateOfPublication, developer, publisher, price, shortDescription);
        GameManagerNeo4j.addGameNode(new Game(name));
        outcomeMessage.setText("Successfully added.");
        return;
    }

}
