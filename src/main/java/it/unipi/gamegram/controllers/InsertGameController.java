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
    private void insert() {
        String name;
        LocalDate dateOfPublication;
        String developer;
        String publisher;
        double price;
        String shortDescription;
        String fullDescription;

        // Retrieve input values from the UI elements
        name = gameName.getText();
        dateOfPublication = gameDateOfPublication.getValue();
        developer = gameDeveloper.getText();
        publisher = gamePublisher.getText();

        // Parse the price value, set to 0 if empty
        if (gamePrice.getText().isEmpty())
            price = 0;
        else
            price = Double.parseDouble(gamePrice.getText());

        shortDescription = gameShortDescription.getText();
        fullDescription = gameFullDescription.getText();

        // Validate the input
        if (name.isEmpty()) {
            outcomeMessage.setText("Insert at least the name.");
            return;
        }

        // Check if the game name already exists
        if (!GameManagerMongoDB.checkGameName(name)) {
            outcomeMessage.setText("Game already exists.");
            return;
        }

        // flag to check which database encountered problems
        boolean flag = true;
        // Insert the game into the database
        try {
            GameManagerMongoDB.insertGame(name, dateOfPublication, developer, publisher, price, shortDescription, fullDescription);
            // MongoDB's operation successfully completed
            flag = false;
            GameManagerNeo4j.addGameNode(new Game(name));
        } catch(Exception e){
            if(!flag)
                // if enters here the database which has problem was Neo4j
                // proceed to delete the game also in MongoDB for consistency
                GameManagerMongoDB.deleteGame(name);
            outcomeMessage.setText("Error while inserting new game");
        }
        outcomeMessage.setText("Successfully added.");
    }
}