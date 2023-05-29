package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import it.unipi.gamegram.Entities.User;

import java.io.IOException;

public class SignupController {

    @FXML
    private Button back;

    @FXML
    private Button signup;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField signUpSurname;

    @FXML
    private TextField signUpNick;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private Label outcomeMessage;

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("start");
    }
    @FXML
    private void signup() throws IOException {
        String password;
        String nick;
        String name;
        String surname;

        nick = signUpNick.getText();
        password = signUpPassword.getText();
        name = signUpName.getText();
        surname = signUpSurname.getText();

        if(nick.isEmpty() || password.isEmpty() || name.isEmpty()|| surname.isEmpty()){
            outcomeMessage.setText("Fill all the fields.");
            return;
        }

        if(nick.contains("'")){
            outcomeMessage.setText("Forbidden characters, retry.");
            return;
        }

        if(!User.checkNick(signUpNick.getText())){
            outcomeMessage.setText("Nick already exists. Choose another one.");
            return;
        }

        if(password.length() < 8){
            outcomeMessage.setText("Weak password (at least 8 characters).");
            return;
        }

        User.register(nick, password, name, surname);
        outcomeMessage.setText("Successfully registered, go back to login.");
        return;
        //GameGramApplication.setRoot("start");
    }

}
