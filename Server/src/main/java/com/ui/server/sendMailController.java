package com.ui.server;

import com.util.SendMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class sendMailController {
    @FXML
    public Button sendBut;
    @FXML
    public TextField subjectfield;
    @FXML
    public TextArea screen;

    public void initialize() {
        sendBut.setDisable(true);
    }


    public void sendButClick(ActionEvent actionEvent) {
        SendMail.getInstance().sendMail(subjectfield.getText(),null,"mail.txt");
        screen.setText("OK");
        sendBut.setDisable(true);
        subjectfield.setText(null);
    }

    public void subjecttyping(KeyEvent keyEvent) {
        if (subjectfield.getText().trim() == "") {
            sendBut.setDisable(true);
        }else sendBut.setDisable(false);
    }
}
