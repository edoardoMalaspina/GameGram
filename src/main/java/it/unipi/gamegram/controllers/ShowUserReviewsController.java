package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersMongoDB.ReviewManagerMongoDB;
import it.unipi.gamegram.singletons.ReviewSingleton;
import it.unipi.gamegram.singletons.UserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import java.io.IOException;
import java.util.List;

public class ShowUserReviewsController {
    @FXML
    TableView<Review> reviewTable = new TableView<>();

    @FXML
    ContextMenu cm = new ContextMenu();

    @FXML
    MenuItem menuItem1 = new MenuItem();

    @FXML
    private Button back;

    @FXML
    private Label title;

    @FXML
    public void initialize() {
        String nickTitle = UserSingleton.getNick();
        title.setText(nickTitle + "'s reviews");

        TableColumn gameCol = new TableColumn("game");
        gameCol.setCellValueFactory(new PropertyValueFactory<>("gameOfReference"));

        TableColumn dateCol = new TableColumn("date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        TableColumn titleCol = new TableColumn("title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        reviewTable.getColumns().addAll(gameCol, dateCol, titleCol);

        ObservableList<Review> olReviews = FXCollections.observableArrayList();

        reviewTable.setItems(olReviews);

        List<Document> reviews = ReviewManagerMongoDB.findReviewByAuthor(nickTitle);

        if (reviews != null) {
            for (Document d : reviews) {
                Review r = new Review(d);
                olReviews.add(r);
            }
        }
    }

    @FXML
    public void showReview() throws IOException {
        ReviewSingleton.setNull();
        ReviewSingleton.getInstance(reviewTable.getSelectionModel().getSelectedItem());
        ReviewSingleton.setFlag(false);
        GameGramApplication.setRoot("reviewpage");
    }

    @FXML
    private void back() throws IOException {
        if (UserSingleton.getFlag())
            GameGramApplication.setRoot("userhome");
        else
            GameGramApplication.setRoot("userpage");
    }
}