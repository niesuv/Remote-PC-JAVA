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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class  sendMailController {
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

    String emoji1 = "✅";
    String emoji2 = "\uD83D\uDCE9"; // Cho 📩
    String emoji3 = "\uD83D\uDD3A"; // Cho 🔺
    String bomaylaymay = "\uD83D\uDE4F"; // Cho 🙏
    public void logText(String log, String color) {
        Text text = new Text(log);
        text.setStyle("-fx-fill: " + color);
        text.setFont(new Font("Monaco", 15));
        text.wrappingWidthProperty().set(540);
        Platform.runLater(() -> {
            if (textOutputs.getHeight() > 380)
                textOutputs.getChildren().clear();
            textOutputs.getChildren().add(text);
        });
    }

    public void initialize() {
        List<String> list = List.of("Snapshot", "KeyLogger", "GetFile",
                "ListDirectory", "ListProcess", "StopProcess", "OpenProcess", "Shutdown");
        comboBox.setItems(FXCollections.observableList(list));
        comboBox.getSelectionModel().selectFirst();
        addField.setEditable(false);
        comboBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    switch (t1) {
                        case "Snapshot" -> {
                            addField.setDisable(true);
                            addField.setEditable(false);
                            labelAddField.setText("Not Available");
                            sendBut.setDisable(false);
                        }
                        case "KeyLogger" -> {
                            addField.setDisable(false);
                            labelAddField.setText("Time to log in milliseconds");
                            sendBut.setDisable(false);
                            addField.setEditable(true);
                        }
                        case "GetFile" -> {
                            addField.setDisable(false);
                            labelAddField.setText("File absolute path");
                            sendBut.setDisable(false);
                            addField.setEditable(true);
                        }
                        case "Shutdown" -> {
                            addField.setDisable(false);
                            labelAddField.setText("Your Password (Optional)");
                            sendBut.setDisable(false);
                            addField.setEditable(true);
                        }
                        case "ListProcess" -> {
                            addField.setDisable(true);
                            addField.setEditable(false);
                            labelAddField.setText("Nothing here");
                            sendBut.setDisable(false);
                        }
                        case "ListDirectory" -> {
                            addField.setDisable(true);
                            addField.setEditable(false);
                            labelAddField.setText("Coming Soon");
                            sendBut.setDisable(false);
                        }
                        default -> sendBut.setDisable(true);
                    }

                }
        );
    }


    public void sendButAction(ActionEvent actionEvent) {
        String choose = comboBox.getSelectionModel().getSelectedItem();
        List<String> colorList = new ArrayList<>();
        colorList.add("pink");
        colorList.add("green");
        colorList.add("orange");
        colorList.add("pink");
        //snapshot task
        comboBox.setDisable(true);
        switch (choose) {
            case "Snapshot" -> {
                int id = random.nextInt(1000, 9999);
                sendBut.setDisable(true);
                int tmp = id % 1000 % 100 % 10;
                while (tmp > 3) tmp -= 3;
                String coloruse = colorList.get(tmp);
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        SendMail.getInstance().sendMail("req / " + id + " / " + "takeshot");
                        logText(emoji1 + "ID: " + id + " Send Mail Successfully! Wait for response!", coloruse);
                        String a = CheckMail.getInstance().listen(id, 40);
                        if (a != null) {
                            logText(emoji2 + "Get Response successfully. File in [ " + a + " ]", coloruse);

                        } else {
                            logText(emoji3 + "ID: " + id + " Error happens", "red");
                        }

                    } catch (IOException | MessagingException e) {
                        logText(emoji3 + e.getMessage(), "red");
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                            logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                            comboBox.setDisable(false);
                        });
                    }

                });
                thread.start();

            }

            //key log task
            case "KeyLogger" -> {
                try {
                    int time = Integer.parseInt(addField.getText());
                    if (time < 0) {
                        throw new NumberFormatException();
                    } else if (time < 1000) {
                        logText(bomaylaymay + "Too small time to listen", "yellow");
                    } else {
                        sendBut.setDisable(true);
                        int id = random.nextInt(1000, 9999);
                        int tmp = id % 1000 % 100 % 10;
                        while (tmp > 3) tmp -= 3;
                        String coloruse = colorList.get(tmp);
                        Thread thread = new Thread(() -> {
                            try {
                                System.out.println(Thread.currentThread().getName());
                                SendMail.getInstance().sendMail("req / " + id + " / " + "keylog /" + time);
                                logText(emoji1 + "ID: " + id + " Send Mail Successfully! Wait for response!", coloruse);
                                String a = CheckMail.getInstance().listen(id, 43 + time / 1000);
                                if (a != null) {
                                    logText(emoji2 + "Get Response successfully. File in " + a, coloruse);
                                } else {
                                    logText(emoji3 + "ID: " + id + "Error happens", "red");
                                }
                            } catch (IOException | MessagingException e) {
                                logText(emoji3 + e.getMessage(), "red");
                                e.printStackTrace();
                            } finally {
                                Platform.runLater(() -> {
                                    sendBut.setDisable(false);
                                    logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                                    comboBox.setDisable(false);
                                });
                            }


                        });
                        thread.start();
                    }
                } catch (NumberFormatException e) {
                    logText(bomaylaymay + "Please enter an valid time!", "red");
                }
            }
            case "Shutdown" -> {
                int id = random.nextInt(1000, 9999);
                String sudopass = addField.getText();
                int tmp = id % 1000 % 100 % 10;
                while (tmp > 3) tmp -= 3;
                String coloruse = colorList.get(tmp);
                sendBut.setDisable(true);
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        SendMail.getInstance().sendMail("req / " + id + " / " + "Shutdown/" + sudopass);
                        logText(emoji1 + "ID: " + id + "Password: " + sudopass + " Send Mail Successfully! If there is a response, It means an error has occured!", coloruse);
                    } catch (IOException | MessagingException e) {
                        logText(emoji3 + e.getMessage(), "red");
                        e.printStackTrace();
                    }finally {
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                            logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                            comboBox.setDisable(false);
                        });
                    }
                });
                thread.start();
            }
            case "ListProcess" -> {
                int id = random.nextInt(1000, 9999);
                int tmp = id % 1000 % 100 % 10;
                while (tmp > 3) tmp -= 3;
                String coloruse = colorList.get(tmp);
                sendBut.setDisable(true);
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        SendMail.getInstance().sendMail("req / " + id + " / " + "ListProcess");
                        logText(emoji1 + "ID: " + id + " Send Mail Successfully! Wait for response!", coloruse);
                        String a = CheckMail.getInstance().listen(id, 40);
                        if (a != null) {
                            logText(emoji2 + "Get Response successfully. File in [ " + a + " ]", coloruse);

                        } else {
                            logText(emoji3 + "ID: " + id + " Error happens", "red");
                        }

                    } catch (Exception e) {
                        logText(emoji3 + e.getMessage(), "red");
                        e.printStackTrace();
                    }finally {
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                            logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                            comboBox.setDisable(false);
                        });
                    }
                });
                thread.start();
            }
            case "ListDirectory" -> {
                int id = random.nextInt(1000, 9999);
                int tmp = id % 1000 % 100 % 10;
                while (tmp > 3) tmp -= 3;
                String coloruse = colorList.get(tmp);
                sendBut.setDisable(true);
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        SendMail.getInstance().sendMail("req / " + id + " / " + "listdir");
                        logText(emoji1 + "ID: " + id + " Send Mail Successfully! Wait for response!", coloruse);
                        String a = CheckMail.getInstance().listen(id, 40);
                        if (a != null) {
                            logText(emoji2 + "Get Response successfully. File in " + a, coloruse);

                        } else {
                            logText(emoji3 + "ID: " + id + " Error happens", "red");
                        }

                    } catch (IOException | MessagingException e) {
                        logText(emoji3 + e.getMessage(), "red");
                        e.printStackTrace();
                    }finally {
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                            logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                            comboBox.setDisable(false);
                        });
                    }
                });
                thread.start();
            }
            case "GetFile" -> {
                int id = random.nextInt(1000, 9999);
                sendBut.setDisable(true);
                int tmp = id % 1000 % 100 % 10;
                while (tmp > 3) tmp -= 3;
                String coloruse = colorList.get(tmp);
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        String filename = addField.getText();
                        if (!Path.of(filename).isAbsolute()) {
                            logText(bomaylaymay+"You must enter an absolute path", "yellow");
                            Platform.runLater(() -> {
                                sendBut.setDisable(false);
                            });
                        } else {
                            SendMail.getInstance().sendMail("req / " + id + " / " + "get/\"" + filename + "\"");
                            logText(emoji1 + "ID: " + id + " Send Mail Successfully! Wait for response!", coloruse);
                            String a = CheckMail.getInstance().listen(id, 40);
                            if (a != null) {
                                if (a.startsWith(".")) {
                                    logText(a.substring(1), "yellow");
                                } else {
                                    logText(emoji2 + "Get Response successfully. File in " + a, coloruse);
                                }

                            } else {
                                logText(emoji3 + "Error happens", "red");
                            }
                        }

                    } catch (IOException | MessagingException e) {
                        logText(emoji3 + e.getMessage(), "red");
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> {
                            sendBut.setDisable(false);
                            logText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", coloruse);
                            comboBox.setDisable(false);
                        });
                    }
                });
                thread.start();
            }
        }

    }

}
