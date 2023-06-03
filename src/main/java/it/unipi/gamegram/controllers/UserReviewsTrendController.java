package it.unipi.gamegram.controllers;

import it.unipi.gamegram.GameGramApplication;
import it.unipi.gamegram.entities.LoggedUser;
import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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


public class UserReviewsTrendController {

    @FXML
    private Button back;

    @FXML
    private AnchorPane pane;

    @FXML
    public void initialize() {

        DefaultCategoryDataset dataset = UserManagerMongoDB.userReviewsTrend(LoggedUser.getLoggedUser());

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

    }

    @FXML
    public void back() throws IOException {
        GameGramApplication.setRoot("trends");
    }
}