package com.ui.server;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class AboutController {

    @FXML
    public BorderPane pane;

    public void initialize() {
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.rgb(255, 255, 255, 0.2),
                null,
                null
        );
        Background background = new Background(backgroundFill);
        pane.setBackground(background);
    }
}
