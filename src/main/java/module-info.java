module com.example.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires org.mongodb.bson;


    opens it.unipi.gamegram to javafx.fxml;
    exports it.unipi.gamegram;
    exports it.unipi.gamegram.entities;
    opens it.unipi.gamegram.entities to javafx.fxml;
    exports it.unipi.gamegram.SceneControllers;
    opens it.unipi.gamegram.SceneControllers to javafx.fxml;
}