package com.ui.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 994, 631);
        stage.setResizable(false);
        stage.setTitle("Remote App!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
    public static void main(String[] args) {
        launch();
    }
}