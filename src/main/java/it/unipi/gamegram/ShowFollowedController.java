package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

public class ShowFollowedController {
    @FXML
    TableView <User> userTable = new TableView< >();

    @FXML
    ContextMenu cm = new ContextMenu();

    @FXML
    MenuItem menuItem1 = new MenuItem();

    @FXML
    private Button back;

    @FXML
    private Label title;

    private ObservableList< User > olUsers;

    @FXML
    public void initialize() {
        User user = LoggedUser.getLoggedUser();
        title.setText(user.getNick() + "'s followed users");

        TableColumn nickCol = new TableColumn("nick");
        nickCol.setCellValueFactory(new PropertyValueFactory< >("nick"));

        TableColumn firstNameCol = new TableColumn("first name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory < > ("firstName"));

        TableColumn lastNameCol = new TableColumn("last name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory < > ("lastName"));

        userTable.getColumns().addAll(nickCol, firstNameCol, lastNameCol);

        olUsers = FXCollections.observableArrayList();

        userTable.setItems(olUsers);


        List<User> users = UserManagerNeo4j.getListFollowedUsers(user);

        for(User d:users){
            olUsers.add(d);
        }
    }

    @FXML
    public void showUserPage() throws IOException{
        UserSingleton.getInstance(userTable.getSelectionModel().getSelectedItem().getNick());
        GameGramApplication.setRoot("userpage");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }
}
