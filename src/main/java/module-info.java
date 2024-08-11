module com.example.encryption3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens com.example.encryption3 to javafx.fxml;
    exports com.example.encryption3;
}