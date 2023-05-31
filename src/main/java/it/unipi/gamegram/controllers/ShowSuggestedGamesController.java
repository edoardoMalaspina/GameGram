package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.GameSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class ShowSuggestedGamesController {
    @FXML
    TableView <Game> gameTable = new TableView< >();

    @FXML
    ContextMenu cm = new ContextMenu();

    @FXML
    MenuItem menuItem1 = new MenuItem();

    @FXML
    private Button back;

    @FXML
    private Label title;

    private ObservableList< Game > olGames;

    @FXML
    public void initialize() {
        User user = LoggedUser.getLoggedUser();
        title.setText("Games you might like");

        TableColumn nameCol = new TableColumn("name");
        nameCol.setCellValueFactory(new PropertyValueFactory< >("name"));

        gameTable.getColumns().addAll(nameCol);

        olGames = FXCollections.observableArrayList();

        gameTable.setItems(olGames);


        List<String> games = UserManagerNeo4j.suggestTrendingNowAmongFollowed(user);

        for(String g:games){
            olGames.add(new Game(g));
        }
    }

    @FXML
    public void showGamePage() throws IOException{
        GameSingleton.setNull();
        GameSingleton.getInstance(gameTable.getSelectionModel().getSelectedItem().getName());
        GameGramApplication.setRoot("gamepage");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}
