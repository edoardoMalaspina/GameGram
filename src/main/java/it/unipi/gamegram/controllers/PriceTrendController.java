package it.unipi.gamegram.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.drivers.MongoDBDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class PriceTrendController {


    @FXML
    private Button back;

    @FXML
    private AnchorPane pane;


    @FXML
    public void initialize() {
        try{
            MongoDBDriver driver = MongoDBDriver.getInstance();
            MongoCollection collection = driver.getCollection("games");

            // Construct the aggregation pipeline
            List<Document> pipeline = Arrays.asList(
                    new Document("$match", new Document()
                            .append("dateOfPublication", new Document("$gte", new Date(2000 - 1900, 0, 1)))
                            .append("dateOfPublication", new Document("$lte", new Date(2022 - 1900, 11, 31)))
                    ),
                    new Document("$group", new Document("_id", new Document("year",
                            new Document("$dateToString", new Document("format", "%Y").append("date", "$dateOfPublication"))))
                            .append("averagePrice", new Document("$avg", "$price"))),
                    new Document("$sort", new Document("_id.year", 1))
            );


            // Execute the aggregation pipeline
            MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
            // Create the dataset for the histogram
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();


            while (cursor.hasNext()) {
                Document result = cursor.next();

                // Access the result fields
                Document yearObj = result.get("_id", Document.class);
                String year = yearObj.getString("year");
                double averagePrice = result.getDouble("averagePrice");
                // Add data to the dataset
                dataset.addValue(averagePrice, "Average Price", year);
            }

            JFreeChart chart = ChartFactory.createBarChart("", "Year",
                    "Average Price", dataset, PlotOrientation.VERTICAL, false, false, false);

            CategoryPlot plot = chart.getCategoryPlot();

            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            Color barColor = Color.decode("#CEB700"); // Red color using HTML code
            renderer.setSeriesPaint(0, barColor);

            Color backgroundColor = Color.decode("#FDFBE2"); // Light gray color using HTML code
            plot.setBackgroundPaint(backgroundColor);

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

            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}
