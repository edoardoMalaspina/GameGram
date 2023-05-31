package it.unipi.gamegram;

import it.unipi.gamegram.Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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




        userTable.getColumns().addAll(nickCol);

        olUsers = FXCollections.observableArrayList();

        userTable.setItems(olUsers);


        List<User> users = ManagerNeo4j.getListFollowedUsers(user);

        for(User d:users){
            olUsers.add(d);
        }
    }

    @FXML
    public void showUserPage() throws IOException{
        UserSingleton.setNull();
        UserSingleton.getInstance(userTable.getSelectionModel().getSelectedItem().getNick());
        GameGramApplication.setRoot("userpage");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }
}
