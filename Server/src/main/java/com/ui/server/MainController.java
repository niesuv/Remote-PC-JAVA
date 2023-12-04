package com.ui.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
    private Parent sendMailView;
    @FXML
    public BorderPane mainPane;
    @FXML
    public Button but1, but4;

    private Parent sendmailPane, aboutPane, docPane;

    public void initialize() {
        try {
            sendmailPane = FXMLLoader.load(getClass().getResource("sendMail.fxml"));
            aboutPane = FXMLLoader.load(getClass().getResource("about.fxml"));
            docPane = FXMLLoader.load(getClass().getResource("document.fxml"));

            System.out.println("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void but1click(ActionEvent actionEvent) {
        //mainPane.setStyle("-fx-background-color: transparent");
        mainPane.setCenter(sendmailPane);
    }

    public void but3click(ActionEvent actionEvent) {
        //mainPane.setStyle("-fx-background-color: transparent");
        mainPane.setCenter(docPane);
    }

    public void but4click(ActionEvent actionEvent) {
        //mainPane.setStyle("-fx-background-color: transparent");
        mainPane.setCenter(aboutPane);
    }
}