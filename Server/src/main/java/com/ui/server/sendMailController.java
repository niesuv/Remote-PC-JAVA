package com.ui.server;

import com.util.CheckMail;
import com.util.SendMail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class sendMailController {
    Random random = new Random();
    @FXML
    public Button sendBut;
    @FXML
    public TextFlow screen;
    @FXML
    public VBox textOutputs;
    @FXML
    public TextField addField;
    @FXML
    public ComboBox<String> comboBox;
    @FXML
    public Label labelAddField;

    public void logText(String log, String color) {
        Text text = new Text(log);
        text.setStyle("-fx-fill: " + color);
        Platform.runLater(() -> {
            textOutputs.getChildren().add(text);
        });
    }

    public void initialize() {
        List<String> list = List.of("Snapshot", "KeyLogger", "ListProcess", "StopProcess", "OpenProcess", "Shutdown");
        comboBox.setItems(FXCollections.observableList(list));
        comboBox.getSelectionModel().selectFirst();
        addField.setEditable(false);
        comboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    if (t1.equals("Snapshot")) {
                        addField.setDisable(true);
                        addField.setEditable(false);
                        labelAddField.setText("Not Available");
                        sendBut.setDisable(false);
                    } else {
                        sendBut.setDisable(true);
                    }
//                    if (t1.equals("KeyLogger")) {
//                        addField.setEditable(true);
//                        addField.setDisable(false);
//                        labelAddField.setText("Time key Log (s)");
//                    } else if (t1.equals("StopProcess") || t1.equals("OpenProcess")) {
//                        addField.setEditable(true);
//                        addField.setDisable(false);
//                        labelAddField.setText("Process ID");
//                        sendBut.setDisable(true);
//                    }
//                    else  {
//                        addField.setDisable(true);
//                        addField.setEditable(false);
//                        labelAddField.setText("Not Available");
//                        sendBut.setDisable(false);
//                    }
                }
        );

    }


    public void sendButAction(ActionEvent actionEvent) {
        String choose = comboBox.getSelectionModel().getSelectedItem();
        if (choose.equals("Snapshot")) {
            int id = random.nextInt(1000, 9999);

            sendBut.setDisable(true);
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    SendMail.getInstance().sendMail("req / " + id + " / " + "takeshot");
                    logText("ID: " + id + " Send Mail Successfully! Wait for response!", "green");
                    String a = CheckMail.getInstance().listen(id);
                    if (a != null) {
                        logText("Get Response successfully. File in " + a, "green");
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                        });
                    } else {
                        logText("Error happens", "red");
                    }
                } catch (IOException | MessagingException e) {
                    logText(e.getMessage(), "red");
                    e.printStackTrace();
                }
            });
            thread.start();


        }
    }

    public void idChange(InputMethodEvent inputMethodEvent) {

    }
}
