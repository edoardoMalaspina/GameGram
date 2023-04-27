module com.example.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;
    requires org.mongodb.bson;


    opens com.example.gamegram to javafx.fxml;
    exports com.example.gamegram;
}