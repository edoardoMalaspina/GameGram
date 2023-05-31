package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.UserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;

public class ShowSuggestedFollowController {
    @FXML
    TableView<User> userTable = new TableView<>();

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
        User user = LoggedUser.getLoggedUser();
        title.setText("Users you might know");

        TableColumn nickCol = new TableColumn("nick");
        nickCol.setCellValueFactory(new PropertyValueFactory<>("nick"));

        userTable.getColumns().addAll(nickCol);

        ObservableList<User> olUsers = FXCollections.observableArrayList();

        userTable.setItems(olUsers);

        List<String> users = UserManagerNeo4j.suggestWhoToFollow(user);

        for (String n : users) {
            olUsers.add(new User(n));
        }
    }

    @FXML
    public void showUserPage() throws IOException {
        UserSingleton.setNull();
        UserSingleton.getInstance(userTable.getSelectionModel().getSelectedItem().getNick());
        GameGramApplication.setRoot("userpage");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}