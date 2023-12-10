module com.ui.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires java.activation;
    requires java.desktop;
    opens com.ui.server to javafx.fxml;
    exports com.ui.server;
}