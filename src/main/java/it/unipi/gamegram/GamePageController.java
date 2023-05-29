package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Game;
import it.unipi.gamegram.Entities.Review;
import it.unipi.gamegram.Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class GamePageController {

    @FXML
    private Label title;

    @FXML
    private Label dateOfPublication;

    @FXML
    private Label developer;

    @FXML
    private Label publisher;

    @FXML
    private Label price;

    @FXML
    private Label shortDescription;

    @FXML
    private Label outcomeMessage;

    @FXML
    private Button like;

    @FXML
    private Button unlike;

    @FXML
    private Button showReviews;

    @FXML
    private Button writeReview;

    @FXML
    private Button back;

    @FXML
    private Label numberOfLikes;

    @FXML
    private Label numberOfReviews;

    @FXML
    private Button delete;
    
    public void initialize() {
        delete.setVisible(false);
        delete.setDisable(true);
        if(LoggedUser.getIsAdmin()) {
            delete.setVisible(true);
            delete.setDisable(false);
        }
        String name = GameSingleton.getName();
        title.setText(name + "'s game page");
        Game game = new Game(Game.findByName(name));
        dateOfPublication.setText("Date of publication: " + game.getDateOfPublication());
        developer.setText("Developer: " + game.getDeveloper());
        publisher.setText("Publisher: " + game.getPublisher());
        price.setText("Price: " + game.getPrice());
        shortDescription.setText("Short description: " + game.getShortDescription());
        numberOfLikes.setText("Number of likes: " + GameManagerNeo4j.countLikes(GameSingleton.getName()));
        numberOfReviews.setText("Number of reviews: " + GameManagerNeo4j.countReviews(GameSingleton.getName()));
    }

    @FXML
    private void showReviews() throws IOException {
        GameGramApplication.setRoot("showgamereviews");
    }

    @FXML
    private void writeReview() throws IOException {
        if(Review.findByGameAndAuthor(GameSingleton.getName(), LoggedUser.getLoggedUser().getNick())){
            outcomeMessage.setText("You already wrote a review for this game.");
            return;
        }
        GameGramApplication.setRoot("writereview");
    }

    @FXML
    private void like() throws IOException {
        Game game = new Game(GameSingleton.getName());
        if(UserManagerNeo4j.checkIfAlreadyLiked(LoggedUser.getLoggedUser(), game)){
            outcomeMessage.setText("Game already liked.");
            return;
        }
        UserManagerNeo4j.addDirectedLinkLike(LoggedUser.getLoggedUser(), game);
        numberOfLikes.setText("Number of likes: " + GameManagerNeo4j.countLikes(GameSingleton.getName()));
        outcomeMessage.setText("Game successfully liked.");
    }

    @FXML
    private void unlike() throws IOException {
        Game game = new Game(GameSingleton.getName());
        if(!UserManagerNeo4j.checkIfAlreadyLiked(LoggedUser.getLoggedUser(), game)){
            outcomeMessage.setText("Game not liked yet.");
            return;
        }
        UserManagerNeo4j.unlike(LoggedUser.getLoggedUser(), game);
        numberOfLikes.setText("Number of likes: " + GameManagerNeo4j.countLikes(GameSingleton.getName()));
        outcomeMessage.setText("Game successfully unliked.");
    }
    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("userhome");}

    @FXML
    private void delete() throws IOException {
        Game.delete(GameSingleton.getName());
        UserManagerNeo4j.deleteGameNode(GameSingleton.getName());
        GameSingleton.setNull();
        GameGramApplication.setRoot("userhome");}

}
