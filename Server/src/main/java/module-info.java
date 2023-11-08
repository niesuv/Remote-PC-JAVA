module com.ui.server {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ui.server to javafx.fxml;
    exports com.ui.server;
}