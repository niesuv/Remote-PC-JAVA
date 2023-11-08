package com.ui.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
    @FXML
    public BorderPane mainPane;
    @FXML
    public Button but1;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void but1click(ActionEvent actionEvent) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("sendMail.fxml"));
            mainPane.setCenter(view);
            but1.setDisable(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}