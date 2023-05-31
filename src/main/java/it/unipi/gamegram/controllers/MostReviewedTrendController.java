package it.unipi.gamegram.controllers;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.drivers.MongoDBDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MostReviewedTrendController {

    @FXML
    private Button back;

    @FXML
    private AnchorPane pane;

    @FXML
    public void initialize() {
        try {
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection<Document> collection = driver.getCollection("games");

            // MongoDB Pipeline
            List<Document> pipeline = Arrays.asList(
                    new Document("$unwind", "$reviews"),
                    new Document("$addFields", new Document("reviewYear", new Document("$year", "$reviews.review_date"))),
                    new Document("$group", new Document("_id", new Document("year", "$reviewYear")
                            .append("game", "$name"))
                            .append("reviewCount", new Document("$sum", 1))),
                    new Document("$sort", new Document("_id.year", 1).append("reviewCount", -1)),
                    new Document("$group", new Document("_id", "$_id.year")
                            .append("mostReviewedGame", new Document("$first", "$_id.game"))
                            .append("maxReviewCount", new Document("$first", "$reviewCount"))),
                    new Document("$sort", new Document("_id", 1))
            );

            // Execution
            AggregateIterable<Document> result = collection.aggregate(pipeline);

            // Create the dataset for the bar chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Document doc : result) {
                int year = doc.getInteger("_id");
                String gameName = doc.getString("mostReviewedGame");
                int reviewCount = doc.getInteger("maxReviewCount");

                dataset.addValue(reviewCount, gameName, String.valueOf(year));
            }

            // Making a plot
            JFreeChart chart = ChartFactory.createBarChart("", "Year", "Review Count",
                    dataset, PlotOrientation.VERTICAL, true, true, false);

            CategoryPlot plot = chart.getCategoryPlot();

            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setDefaultItemLabelsVisible(true);
            renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
            plot.setBackgroundPaint(Color.decode("#FDFBE2"));

            // Putting it into an ImageView for visualization
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