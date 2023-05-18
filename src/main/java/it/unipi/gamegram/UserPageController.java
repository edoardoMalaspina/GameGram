package it.unipi.gamegram;

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
    private Label isadmin;

    @FXML
    private Button follow;

    @FXML
    private Button unfollow;

    @FXML
    private Button showFollows;

    @FXML
    private Button showReviews;

    @FXML
    private Button back;

    public void initialize() {
        String nickTitle = UserSingleton.getNick();
        User user = new User(User.findByNick(nickTitle));
        title.setText(nickTitle + "'s user page");
        nick.setText("Nick: " + nickTitle);
        name.setText("Name: " + user.getFirstName());
        isadmin.setText(("Admin: " + user.getIsAdmin()));
    }

    @FXML
    private void showReviews() throws IOException {
        UserSingleton.setNull();
        UserSingleton.getInstance(LoggedUser.getLoggedUser().getNick());
        GameGramApplication.setRoot("showuserreviews");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
