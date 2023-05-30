package it.unipi.gamegram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class TrendsController {

    @FXML
    private Button suggestedLikes;

    @FXML
    private Button suggestedFollows;

    @FXML
    private Button showActiveFollowed;

    @FXML
    private Button mongo1;

    @FXML
    private Button mongo2;

    @FXML
    private Button mongo3;

    @FXML
    private Button back;

    @FXML
    private void SuggestedLikes() throws IOException {
        GameGramApplication.setRoot("showsuggestedgames");
    }

    @FXML
    private void ShowActiveFollowed() throws IOException {
        GameGramApplication.setRoot("showactivefollowed");
    }

    @FXML
    private void SuggestedFollows() throws IOException {
        GameGramApplication.setRoot("showsuggestedfollow");
    }

    @FXML
    private void mongo1() throws IOException {
        //GameGramApplication.setRoot("");
    }

    @FXML
    private void mongo2() throws IOException {
        //GameGramApplication.setRoot("");
    }
    @FXML
    private void mongo3() throws IOException {
        //GameGramApplication.setRoot("");
    }


    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
