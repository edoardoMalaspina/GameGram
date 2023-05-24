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
    private Label isadmin;

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
    private void delete() throws IOException {
        User.delete(UserSingleton.getNick());
        UserSingleton.setNull();
        GameGramApplication.setRoot("userhome");}

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
