module it.unipi.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;
    requires java.desktop;


    opens it.unipi.gamegram to javafx.fxml;
    exports it.unipi.gamegram;
    exports it.unipi.gamegram.entities;
    opens it.unipi.gamegram.entities to javafx.fxml;
    exports it.unipi.gamegram.drivers;
    opens it.unipi.gamegram.drivers to javafx.fxml;
    exports it.unipi.gamegram.managersMongoDB;
    opens it.unipi.gamegram.managersMongoDB to javafx.fxml;
    exports it.unipi.gamegram.managersNeo4j;
    opens it.unipi.gamegram.managersNeo4j to javafx.fxml;
    exports it.unipi.gamegram.singletons;
    opens it.unipi.gamegram.singletons to javafx.fxml;
    exports it.unipi.gamegram.controllers;
    opens it.unipi.gamegram.controllers to javafx.fxml;
    exports it.unipi.gamegram.utility;
    opens it.unipi.gamegram.utility to javafx.fxml;
    exports it.unipi.gamegram.test;
    opens it.unipi.gamegram.test to javafx.fxml;
}