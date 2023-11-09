package com.util;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;

public class MainClient {

    public static void processRequest(String header) {
        String[] parts = header.split("/");
        System.out.println(Arrays.toString(parts));
        int id = Integer.parseInt(parts[1].trim());
        String command = parts[2].trim();
        if (command.equalsIgnoreCase("takeshot")) {
            String subject = "res/" + id;
            try {
                SendMail.getInstance().sendMail(subject,null,
                        Screenshot.getInstance().takeScreenShot()
                        );
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        CheckMail.getInstance().listen();
    }
}