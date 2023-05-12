package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import it.unipi.gamegram.Entities.User;

import java.io.IOException;

public class UserHomeController {
    @FXML
    private Button findUser;

    @FXML
    private TextField userNick;

    @FXML
    private Label errorMessage;

    public void initialize() {
        errorMessage.setVisible(false);
    }

    @FXML
    private void findUser() throws IOException {
        String nick = userNick.getText();

        if (nick.isEmpty()) {
            errorMessage.setText("Nick is missing.");
            errorMessage.setVisible(true);
            return;
        }
        if(User.checkNick(nick)){
            errorMessage.setText("No such user.");
            errorMessage.setVisible(true);
            return;
        }
        UserSingleton user = UserSingleton.getInstance(nick);
        GameGramApplication.setRoot("userpage");
    }
}
