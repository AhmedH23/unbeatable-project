module com.example.unbeatableproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;

    opens com.example.unbeatableproject to javafx.fxml;
    exports com.example.unbeatableproject;
}

