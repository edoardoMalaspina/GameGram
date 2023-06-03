package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.entities.User;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import it.unipi.gamegram.singletons.UserSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class UserPageController {

    @FXML
    private Label title;

    @FXML
    private Label nick;

    @FXML
    private Label name;

    @FXML
    private Label lastName;

    @FXML
    private Label isadmin;

    @FXML
    private Label outcomeMessage;

    @FXML
    private Button follow;

    @FXML
    private Button unfollow;

    @FXML
    private Button showLiked;

    @FXML
    private Button showReviews;

    @FXML
    private Button promoteAdmin;

    @FXML
    private Button back;

    @FXML
    private Button delete;

    public void initialize() {
        // Check what to display
        delete.setVisible(false);
        delete.setDisable(true);
        promoteAdmin.setVisible(false);
        promoteAdmin.setDisable(true);
        if (LoggedUser.getIsAdmin()) {
            delete.setVisible(true);
            delete.setDisable(false);
            promoteAdmin.setVisible(true);
            promoteAdmin.setDisable(false);
        }
        if (LoggedUser.getLoggedUser().getNick().equals(UserSingleton.getNick())){
            delete.setVisible(true);
            delete.setDisable(false);
        }
        String nickTitle = UserSingleton.getNick();
        User user = new User(UserManagerMongoDB.findUserByNick(nickTitle));
        title.setText(nickTitle + "'s user page");
        nick.setText("Nick: " + nickTitle);
        name.setText("Name: " + user.getFirstName());
        lastName.setText("Last name: " + user.getLastName());
        isadmin.setText("Admin: " + user.getIsAdmin());
    }

    @FXML
    private void showReviews() throws IOException {
        UserSingleton.setFlag(false);
        GameGramApplication.setRoot("showuserreviews");
    }

    @FXML
    private void showLiked() throws IOException {
        UserSingleton.setFlag(false);
        GameGramApplication.setRoot("showliked");
    }

    @FXML
    private void follow() {
        // Check if logged user is on his own page
        if (LoggedUser.getLoggedUser().getNick().equals(UserSingleton.getNick())) {
            outcomeMessage.setText("You can't follow yourself.");
            return;
        }
        User user = new User(UserSingleton.getNick());
        if (UserManagerNeo4j.checkIfAlreadyFollowed(LoggedUser.getLoggedUser(), user)) {
            outcomeMessage.setText("User already followed.");
            return;
        }
        UserManagerNeo4j.follow(LoggedUser.getLoggedUser(), user);
        outcomeMessage.setText("User successfully followed.");
    }

    @FXML
    private void unfollow() {
        if (LoggedUser.getLoggedUser().getNick().equals(UserSingleton.getNick())) {
            outcomeMessage.setText("You can't unfollow yourself.");
            return;
        }
        User user = new User(UserSingleton.getNick());
        if (!UserManagerNeo4j.checkIfAlreadyFollowed(LoggedUser.getLoggedUser(), user)) {
            outcomeMessage.setText("You don't follow this user yet.");
            return;
        }
        UserManagerNeo4j.unfollow(LoggedUser.getLoggedUser(), user);
        outcomeMessage.setText("User successfully unfollowed.");
    }

    @FXML
    private void delete() throws IOException {
        if (UserSingleton.getNick().equals(LoggedUser.getLoggedUser().getNick())) {
            UserManagerMongoDB.deleteUser(UserSingleton.getNick());
            UserSingleton.setNull();
            LoggedUser.logOut();
            GameGramApplication.setRoot("start");
        }
        UserManagerMongoDB.deleteUser(UserSingleton.getNick());
        UserManagerNeo4j.deleteUserNode(UserSingleton.getNick());
        UserSingleton.setNull();
        GameGramApplication.setRoot("userhome");
    }

    @FXML
    private void promoteAdmin() {
        User user = new User(UserManagerMongoDB.findUserByNick(UserSingleton.getNick()));
        if (user.getIsAdmin().equals("Yes")) {
            outcomeMessage.setText("User already admin.");
            return;
        }
        user.promoteAdmin();
        user.setIsAdmin("Yes");
        isadmin.setText("Admin: " + user.getIsAdmin());
        outcomeMessage.setText("Successfully promoted.");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }
}