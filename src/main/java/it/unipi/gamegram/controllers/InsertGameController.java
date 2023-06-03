package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.GameManagerNeo4j;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

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
    private TextField gameFullDescription;

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
        String fullDescription;

        name = gameName.getText();
        dateOfPublication = gameDateOfPublication.getValue();
        developer = gameDeveloper.getText();
        publisher = gamePublisher.getText();
        if(gamePrice.getText().isEmpty())
            price = 0;
        else
            price = Double.parseDouble(gamePrice.getText());
        shortDescription = gameShortDescription.getText();
        fullDescription = gameFullDescription.getText();

        if(name.isEmpty()){
            outcomeMessage.setText("Insert at least the name.");
            return;
        }

        if(!GameManagerMongoDB.checkGameName(name)){
            outcomeMessage.setText("Game already exists.");
            return;
        }

        try {
            GameManagerMongoDB.insertGame(name, dateOfPublication, developer, publisher, price, shortDescription, fullDescription);
            GameManagerNeo4j.addGameNode(new Game(name));
            outcomeMessage.setText("Successfully added.");
            return;
        } catch (Exception e){
            outcomeMessage.setText("there is a problem, insertion aborted");
        }
    }

}
