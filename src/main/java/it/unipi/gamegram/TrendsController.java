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
    private Button priceTrend;

    @FXML
    private Button userReviewsTrend;

    @FXML
    private Button mostReviewedTrend;

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
    private void priceTrend() throws IOException {
        GameGramApplication.setRoot("pricetrend");
    }

    @FXML
    private void userReviewsTrend() throws IOException {
        GameGramApplication.setRoot("userreviewstrend");
    }

    @FXML
    private void mostReviewedTrend() throws IOException {
        GameGramApplication.setRoot("mostreviewedtrend");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");
    }

}
