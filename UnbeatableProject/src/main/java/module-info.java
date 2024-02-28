module com.example.unbeatableproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.unbeatableproject to javafx.fxml;
    exports com.example.unbeatableproject;
}