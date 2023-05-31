package it.unipi.gamegram.controllers;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.drivers.MongoDBDriver;
import it.unipi.gamegram.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class UserReviewsTrendController {

    @FXML
    private Button back;

    @FXML
    private AnchorPane pane;

    @FXML
    public void initialize() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = driver.getCollection("users");

            LoggedUser.getInstance();
            User loggedUser = LoggedUser.getLoggedUser();

            // Construct the aggregation pipeline
            List<Document> pipeline = Arrays.asList(
                    new Document("$match", new Document("nick", loggedUser.getNick())),
                    new Document("$unwind", "$reviews"),
                    new Document("$group", new Document("_id", new Document("year",
                            new Document("$dateToString", new Document("format", "%Y").append("date", "$reviews.review_date"))))
                            .append("reviewCount", new Document("$sum", 1))),
                    new Document("$sort", new Document("_id.year", 1))
            );

            // Execute the aggregation pipeline
            AggregateIterable<Document> result = collection.aggregate(pipeline);

            // Create the dataset for the histogram
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Document document : result) {
                Document yearObj = document.get("_id", Document.class);
                String year = yearObj.getString("year");
                int reviewCount = document.getInteger("reviewCount");

                // Add data to the dataset
                dataset.addValue(reviewCount, "Review Count", year);
            }

            // Make a  plot
            JFreeChart chart = ChartFactory.createBarChart("", "Year",
                    "Review Count", dataset, PlotOrientation.VERTICAL, false, false, false);

            CategoryPlot plot = chart.getCategoryPlot();

            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            Color barColor = Color.decode("#CEB700"); // Red color using HTML code
            renderer.setSeriesPaint(0, barColor);

            Color backgroundColor = Color.decode("#FDFBE2"); // Light gray color using HTML code
            plot.setBackgroundPaint(backgroundColor);

            // Making an ImageView of the plot for visualization
            BufferedImage chartImage = chart.createBufferedImage(600, 300);

            File tempFile;
            try {
                tempFile = File.createTempFile("chart", ".png");
                ImageIO.write(chartImage, "png", tempFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Image image = new Image(tempFile.toURI().toString());

            ImageView imageView = new ImageView(image);

            pane.getChildren().add(imageView);

            tempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}