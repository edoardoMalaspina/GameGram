module com.example.gamegram {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.neo4j.driver;


    opens com.example.gamegram to javafx.fxml;
    exports com.example.gamegram;
}