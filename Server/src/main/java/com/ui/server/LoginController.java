package com.ui.server;

import com.util.CheckMail;
import com.util.SendMail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class LoginController {
    public MainController mainController;


    @FXML
    public TextField textField, passwordField;

    @FXML
    public Button loginButton;

    @FXML
    public Text log;
    @FXML
    public BorderPane mainPain;

    public void initialize() {

    }

    public void loginBut(ActionEvent actionEvent) {
        loginButton.setDisable(true);
        String email = textField.getText();
        String password = passwordField.getText();
        if (!email.isBlank() && !password.isBlank()) {
            String _email = email.trim();
            String _password = password.trim();
            new Thread(() -> {
                Platform.runLater(() -> {
                log.setText("Checking Credentials...");
                log.setStyle("-fx-fill: blue");
            });
               String a = SendMail.getInstance().newCredential(_email, _password);
               if (a.equals("OK")) {
                   a = CheckMail.getInstance().loadCredential();
                   System.out.println(a);
                   if (a.equals("OK")) {
                       Platform.runLater(() -> {
                           log.setText(null);
                           mainController.loadUserPane();
                           mainController.enableBut();
                           mainController.but2click(null);
                       });
                       Thread.currentThread().interrupt();
                   }
                   else {
                       String logText = a;
                       Platform.runLater(() -> {
                           log.setText(logText);
                           loginButton.setDisable(false);
                           log.setStyle("-fx-fill: red");
                       });
                   }
               } else {
                   String logText = a;
                   Platform.runLater(() -> {
                       log.setText(logText);
                       loginButton.setDisable(false);
                       log.setStyle("-fx-fill: red");
                   });
               }
            }).start();

        } else {
            log.setText("Invalid Credentials");
            loginButton.setDisable(false);
            log.setStyle("-fx-fill: red");
        }

    }
}
