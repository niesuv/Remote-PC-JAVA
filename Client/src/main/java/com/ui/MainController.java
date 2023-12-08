package com.ui;

import com.util.CheckMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.util.MainClient.BANNER;

public class MainController {
    public static ArrayList<String> listEmailString;

    @FXML
    public VBox listEmail;
    @FXML
    public Button addButton;

    @FXML
    public TextField emailField;
    private HBox box;

    public HBox createRecord(String s) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("choice.fxml"));
        try {
            HBox box = fxmlLoader.load();
            ((Text) box.getChildren().getFirst()).setText(s);
            ((Button) box.getChildren().getLast()).setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                HBox wrapper = null;
                for (var a : listEmail.getChildren()) {
                    if (((HBox) a).getChildren().getLast() == btn)
                        wrapper = (HBox) a;
                }
                String email = ((Text)wrapper.getChildren().getFirst()).getText().trim();
                listEmail.getChildren().remove(wrapper);
                listEmailString.remove(email);
            });
            return box;
        } catch (IOException e) {
            System.exit(1);
        }
        return null;
    }

    @FXML
    public Button listenBut;

    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("choice.fxml"));

        HBox box = fxmlLoader.load();
        ((Text) box.getChildren().getFirst()).setText("ailearning.hcmus@gmail.com");
        ((Button) box.getChildren().getLast()).setOnAction(actionEvent -> {
            Button btn = (Button) actionEvent.getSource();
            HBox wrapper = null;
            for (var a : listEmail.getChildren()) {
                if (((HBox) a).getChildren().getLast() == btn)
                    wrapper = (HBox) a;
            }
            listEmail.getChildren().remove(wrapper);
        });
        listEmail.getChildren().addFirst(box);
        listEmailString = new ArrayList<>(List.of("ailearning.hcmus@gmail.com"));
    }

    public void listenClick(ActionEvent actionEvent) {
        listenBut.setDisable(true);
        for (var a : listEmail.getChildren()) {
            HBox a1 = (HBox) a;
            a1.getChildren().getLast().setDisable(true);
        }
        new Thread(() -> {
            System.out.println(BANNER);
            System.out.println("Listeningg for request...");
            CheckMail.getInstance().listen();
        }).start();
    }

    public void addRecord(ActionEvent actionEvent) {
        String a = emailField.getText();
        if (a != null && !a.isBlank()) {
            var childs = listEmail.getChildren();
            listEmailString.add(a.trim());
            childs.addFirst(createRecord(a.trim()));
            emailField.setText(null);
        }
    }
}
