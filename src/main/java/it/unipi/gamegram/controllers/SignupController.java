package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import it.unipi.gamegram.managersNeo4j.UserManagerNeo4j;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        if(!UserManagerMongoDB.checkNick(signUpNick.getText())){
            outcomeMessage.setText("Nick already exists. Choose another one.");
            return;
        }

        if(password.length() < 8){
            outcomeMessage.setText("Weak password (at least 8 characters).");
            return;
        }

        try {
            UserManagerMongoDB.register(nick, password, name, surname);
            UserManagerNeo4j.addUserNode(nick);
        } catch (Exception e){
            outcomeMessage.setText("Error while registering user");
        }
        outcomeMessage.setText("Successfully registered, go back to login.");
        return;
    }

}
