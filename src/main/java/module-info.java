module com.example.scene3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;
    opens com.example.scene3 to javafx.fxml;
    exports com.example.scene3;
}