module it.unipi.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;


    opens it.unipi.gamegram to javafx.fxml;
    exports it.unipi.gamegram;
    exports it.unipi.gamegram.Entities;
    opens it.unipi.gamegram.Entities to javafx.fxml;
}