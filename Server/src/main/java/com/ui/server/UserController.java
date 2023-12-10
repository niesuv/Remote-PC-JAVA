package com.ui.server;

import com.util.SendMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class UserController {

    public MainController mainController;

    @FXML
    public Button logoutBut;

    @FXML
    public Text username;

    public void initialize() {
        username.setText("Welcome " + SendMail.getInstance().getGmailAccountName());
    }
    public void logout(ActionEvent actionEvent) {
        SendMail.getInstance().logOut();
        mainController.diasbleBut();
        mainController.but2click(null);

    }
}
