package com.ui.server;

import com.util.SendMail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
    @FXML
    public BorderPane mainPane;
    @FXML
    public Button but1, but4, but3;

    private Parent sendmailPane, aboutPane, docPane, loginPane, userPane;

    public void initialize() {
        try {
            sendmailPane = FXMLLoader.load(getClass().getResource("sendMail.fxml"));
            aboutPane = FXMLLoader.load(getClass().getResource("about.fxml"));
            docPane = FXMLLoader.load(getClass().getResource("document.fxml"));

            FXMLLoader loginLoad = new FXMLLoader(getClass().getResource("login.fxml"));
            loginPane = loginLoad.load();
            ((LoginController) loginLoad.getController()).mainController = this;
            but2click(null);
            System.out.println("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void diasbleBut() {
        Platform.runLater(() -> {
            but1.setDisable(true);
        });
    }

    public void enableBut() {
        Platform.runLater(() -> {
            but1.setDisable(false);
        });
    }

    public void but1click(ActionEvent actionEvent) {
        //mainPane.setStyle("-fx-background-color: transparent");
        mainPane.setCenter(sendmailPane);
    }

    public void but3click(ActionEvent actionEvent) {
        //mainPane.setStyle("-fx-background-color: transparent");
        mainPane.setCenter(docPane);
    }


    //login pane
    public void but2click(ActionEvent actionEvent) {
        if (!SendMail.getInstance().logIn) {
            diasbleBut();
            mainPane.setCenter(loginPane);
        } else {
            mainPane.setCenter(userPane);
        }
    }

    public void but4click(ActionEvent actionEvent) {
        mainPane.setCenter(aboutPane);
    }

    public void loadUserPane() {
        try {
            FXMLLoader userLoad = new FXMLLoader(getClass().getResource("user.fxml"));
            userPane = userLoad.load();
            ((UserController) userLoad.getController()).mainController = this;


        } catch (IOException e) {
            System.out.println("err");
            throw new RuntimeException(e);
        }

    }
}