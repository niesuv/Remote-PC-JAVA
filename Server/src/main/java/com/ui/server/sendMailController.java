package com.ui.server;

import com.util.SendMail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.mail.MessagingException;
import java.io.IOException;

public class sendMailController {
    @FXML
    public Button sendBut;
    @FXML
    public TextField subjectfield;
    @FXML
    public TextFlow screen;
    @FXML
    public VBox textOutputs;

    public void initialize() {
        sendBut.setDisable(true);
    }


    public void sendButClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            sendBut.setDisable(true);
            subjectfield.setText(null);
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Platform.runLater(() -> {
                        sendBut.setDisable(true);
                        subjectfield.setEditable(false);
                        subjectfield.setOnKeyReleased(null);
                    });
                    SendMail.getInstance()
                            .sendMail(subjectfield.getText(), null, "mail.txt");
                    Text oktext = new Text("Send Successfully");
                    oktext.setStyle("-fx-font-size:14");
                    oktext.setStyle("-fx-fill: green");
                    Platform.runLater(() -> {
                        textOutputs.getChildren().add(oktext);

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    Text bugtext = new Text(e.getMessage());
                    bugtext.setStyle("-fx-font-size:14");
                    bugtext.setStyle("-fx-fill: red");
                    Platform.runLater(() -> {
                        textOutputs.getChildren().add(bugtext);
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    sendBut.setDisable(true);
                    subjectfield.setText(null);
                    subjectfield.setEditable(true);
                    subjectfield.setOnKeyReleased(sendMailController.this::subjecttyping);
                }

            }
        }).start();
    }

    public void subjecttyping(KeyEvent keyEvent) {
        if (subjectfield.getText() != null && subjectfield.getText().trim().isEmpty()) {
            sendBut.setDisable(true);
        } else {
            sendBut.setDisable(false);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!sendBut.isDisable()) {
                sendButClick(null);
            }
        }

    }

}
