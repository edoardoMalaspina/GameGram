package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.managersMongoDB.GameManagerMongoDB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

public class MostReviewedTrendController {

    @FXML
    private Button back;

    @FXML
    private AnchorPane pane;

    @FXML
    public void initialize() {

        DefaultCategoryDataset dataset = GameManagerMongoDB.mostReviewedPerYear();

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

    }

    @FXML
    public void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}