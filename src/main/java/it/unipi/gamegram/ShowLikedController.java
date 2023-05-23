package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class ShowLikedController {
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
        User user = new User(UserSingleton.getNick());
        title.setText(user.getNick() + "'s liked games");

        TableColumn nameCol = new TableColumn("name");
        nameCol.setCellValueFactory(new PropertyValueFactory< >("name"));

        gameTable.getColumns().addAll(nameCol);

        olGames = FXCollections.observableArrayList();

        gameTable.setItems(olGames);


        List<Game> games = UserManagerNeo4j.getListLikedGames(user);

        for(Game g:games){
            olGames.add(g);
        }
    }

    @FXML
    public void showGamePage() throws IOException{
        UserSingleton.getInstance(gameTable.getSelectionModel().getSelectedItem().getName());
        GameGramApplication.setRoot("gamepage");
    }

    @FXML
    private void back() throws IOException {
        if(UserSingleton.getFlag())
            GameGramApplication.setRoot("userhome");
        else
            GameGramApplication.setRoot("userpage");
    }
}
