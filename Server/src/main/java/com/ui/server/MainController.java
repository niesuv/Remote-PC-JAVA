package com.ui.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainController {
    private Parent sendMailView;
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

    public void initialize() {
        var pool = Executors.newFixedThreadPool(1);
        var a = pool.submit(new Callable<Parent>() {
            @Override
            public Parent call() {
                try {
                    return FXMLLoader.load(getClass().getResource("sendMail.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            this.sendMailView = a.get();
            System.out.println("Get Successfully");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    public void but1click(ActionEvent actionEvent) {
        mainPane.setCenter(sendMailView);
        but1.setDisable(true);

    }
}