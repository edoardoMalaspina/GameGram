module com.example.gamegram {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamegram to javafx.fxml;
    exports com.example.gamegram;
}