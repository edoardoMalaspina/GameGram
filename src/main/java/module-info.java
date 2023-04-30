module com.example.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires org.mongodb.bson;


    opens it.unipi.gamegram to javafx.fxml;
    exports it.unipi.gamegram;
    exports it.unipi.gamegram.Entities;
    opens it.unipi.gamegram.Entities to javafx.fxml;
}