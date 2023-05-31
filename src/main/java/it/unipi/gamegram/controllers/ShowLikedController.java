package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Game;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.GameSingleton;
import it.unipi.gamegram.singletons.UserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;

public class ShowLikedController {
    @FXML
    TableView<Game> gameTable = new TableView<>();

    @FXML
    ContextMenu cm = new ContextMenu();

    @FXML
    MenuItem menuItem1 = new MenuItem();

    @FXML
    private Button back;

    @FXML
    private Label title;

    @FXML
    public void initialize() {
        User user = new User(UserSingleton.getNick());
        title.setText(user.getNick() + "'s liked games");

        TableColumn nameCol = new TableColumn("name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        gameTable.getColumns().addAll(nameCol);

        ObservableList<Game> olGames = FXCollections.observableArrayList();

        gameTable.setItems(olGames);

        List<Game> games = UserManagerNeo4j.getListLikedGames(user);

        olGames.addAll(games);
    }

    @FXML
    public void showGamePage() throws IOException {
        GameSingleton.setNull();
        GameSingleton.getInstance(gameTable.getSelectionModel().getSelectedItem().getName());
        GameGramApplication.setRoot("gamepage");
    }

    @FXML
    private void back() throws IOException {
        if (UserSingleton.getFlag())
            GameGramApplication.setRoot("userhome");
        else
            GameGramApplication.setRoot("userpage");
    }
}