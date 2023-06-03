package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.Review;
import it.unipi.gamegram.managersMongoDB.ReviewManagerMongoDB;
import it.unipi.gamegram.singletons.GameSingleton;
import it.unipi.gamegram.singletons.ReviewSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import java.io.IOException;
import java.util.List;

public class ShowGameReviewsController {
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
        ObservableList<Review> olReviews;

        String gameTitle = GameSingleton.getName();
        title.setText(gameTitle + "'s reviews");

        TableColumn authorCol = new TableColumn("author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn dateCol = new TableColumn("date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        TableColumn titleCol = new TableColumn("title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        reviewTable.getColumns().addAll(authorCol, dateCol, titleCol);

        olReviews = FXCollections.observableArrayList();

        reviewTable.setItems(olReviews);

        List<Document> reviews = ReviewManagerMongoDB.findReviewByGame(gameTitle);

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
        ReviewSingleton.setFlag(true);
        GameGramApplication.setRoot("reviewpage");
    }

    @FXML
    private void back() throws IOException {
        GameGramApplication.setRoot("gamepage");
    }
}