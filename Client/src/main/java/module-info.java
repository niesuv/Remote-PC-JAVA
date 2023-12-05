module com.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires java.activation;
    requires java.desktop;
    requires com.github.kwhat.jnativehook;

    opens com.ui to javafx.fxml;
    exports com.ui;
}