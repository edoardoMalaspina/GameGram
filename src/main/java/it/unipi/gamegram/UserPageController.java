package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
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
    private Button back;

    @FXML
    private Button delete;

    public void initialize() {
        delete.setVisible(false);
        delete.setDisable(true);
        if(LoggedUser.getIsAdmin()) {
            delete.setVisible(true);
            delete.setDisable(false);
        }
        String nickTitle = UserSingleton.getNick();
        User user = new User(User.findByNick(nickTitle));
        title.setText(nickTitle + "'s user page");
        nick.setText("Nick: " + nickTitle);
        name.setText("Name: " + user.getFirstName());
        lastName.setText("Last name: " + user.getLastName());
        isadmin.setText(("Admin: " + user.getIsAdmin()));
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
    private void follow() throws IOException {
        if(LoggedUser.getLoggedUser().getNick().equals(UserSingleton.getNick())){
            outcomeMessage.setText("You can't follow yourself.");
            return;
        }
        User user = new User(UserSingleton.getNick());
        if(UserManagerNeo4j.checkIfAlreadyFollowed(LoggedUser.getLoggedUser(), user)){
            outcomeMessage.setText("User already followed.");
            return;
        }
        UserManagerNeo4j.addDirectedLinkFollow(LoggedUser.getLoggedUser(), user);
        outcomeMessage.setText("User successfully followed.");
    }

    @FXML
    private void unfollow() throws IOException {
        if(LoggedUser.getLoggedUser().getNick().equals(UserSingleton.getNick())){
            outcomeMessage.setText("You can't unfollow yourself.");
            return;
        }
        User user = new User(UserSingleton.getNick());
        if(!UserManagerNeo4j.checkIfAlreadyFollowed(LoggedUser.getLoggedUser(), user)){
            outcomeMessage.setText("You don't follow this user yet.");
        return;
        }
        UserManagerNeo4j.unfollow(LoggedUser.getLoggedUser(), user);
        outcomeMessage.setText("User successfully unfollowed.");
    }

    @FXML
    private void delete() throws IOException {
        if(UserSingleton.getNick().equals(LoggedUser.getLoggedUser().getNick())){
            User.delete(UserSingleton.getNick());
            UserSingleton.setNull();
            LoggedUser.logOut();
            GameGramApplication.setRoot("start");}
        User.delete(UserSingleton.getNick());
        UserSingleton.setNull();
        GameGramApplication.setRoot("userhome");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
