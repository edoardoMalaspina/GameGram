package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import it.unipi.gamegram.singletons.GameSingleton;
import it.unipi.gamegram.singletons.UserSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserHomeController {
    @FXML
    private Button findUser;

    @FXML
    private Button showFollowed;

    @FXML
    private Button findGame;

    @FXML
    private Button logout;

    @FXML
    private Button showReviews;

    @FXML
    private Button showLikes;

    @FXML
    private Button trends;

    @FXML
    private Button insert;

    @FXML
    private TextField userNick;

    @FXML
    private TextField gameName;

    @FXML
    private Label errorMessage;

    public void initialize() {
        insert.setVisible(false);
        insert.setDisable(true);
        if(LoggedUser.getIsAdmin()) {
            insert.setVisible(true);
            insert.setDisable(false);
        }
        errorMessage.setVisible(false);
    }

    @FXML
    private void findUser() throws IOException {

        UserSingleton.setNull();
        String nick = userNick.getText();

        if (nick.isEmpty()) {
            errorMessage.setText("Nick is missing.");
            errorMessage.setVisible(true);
            return;
        }
        if(UserManagerMongoDB.checkNick(nick)){
            errorMessage.setText("No such user.");
            errorMessage.setVisible(true);
            return;
        }
        UserSingleton user = UserSingleton.getInstance(nick);
        GameGramApplication.setRoot("userpage");
    }

    @FXML
    private void findGame() throws IOException {

        GameSingleton.setNull();
        String name = gameName.getText();

        if (name.isEmpty()) {
            errorMessage.setText("Name is missing.");
            errorMessage.setVisible(true);
            return;
        }
        if(GameManagerMongoDB.checkGameName(name)){
            errorMessage.setText("No such game.");
            errorMessage.setVisible(true);
            return;
        }
        GameSingleton game = GameSingleton.getInstance(name);
        GameGramApplication.setRoot("gamepage");
    }

    @FXML
    private void showReviews() throws IOException {
        UserSingleton.setNull();
        UserSingleton.getInstance(LoggedUser.getLoggedUser().getNick());
        UserSingleton.setFlag(true);
        GameGramApplication.setRoot("showuserreviews");
    }
    @FXML
    private void showFollowed() throws IOException {
        GameGramApplication.setRoot("showfollowed");
    }

    @FXML
    private void showLiked() throws IOException {
        UserSingleton.setNull();
        UserSingleton.getInstance(LoggedUser.getLoggedUser().getNick());
        UserSingleton.setFlag(true);
        GameGramApplication.setRoot("showliked");
    }


    @FXML
    private void insertGame() throws IOException {
        GameGramApplication.setRoot("insertgame");
    }

    @FXML
    private void trends() throws IOException {
        GameGramApplication.setRoot("trends");
    }

    @FXML
    private void logout() throws IOException {
        LoggedUser.logOut();
        GameGramApplication.setRoot("start");
    }

}
