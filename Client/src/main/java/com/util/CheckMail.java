package com.util;

import com.ui.MainController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.util.Arrays;
import java.util.Properties;


public class CheckMail {

    private String username, password;
    private Store store;
    public int count;
    private static CheckMail instance = new CheckMail();

    public static CheckMail getInstance() {
        return instance;
    }

    private CheckMail() {
        try {
            getMail();
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties);
            this.store = emailSession.getStore("pop3s");
            store.connect("pop.gmail.com", username, password);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            this.username = br.readLine().trim();
            this.password = br.readLine().trim();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        Folder emailFolder = null;
                        emailFolder = store.getFolder("INBOX");
                        emailFolder.open(Folder.READ_WRITE);
                        Message[] ls = emailFolder.getMessages();
                        boolean first = true;
                        for (int i = ls.length - 1; i >= 0; i--) {
                            var m = ls[i];
                            String subject = m.getSubject();
                            if (subject.startsWith("req") && first) {
                                m.setFlag(Flags.Flag.DELETED, true);
                                Address fromAddress = m.getFrom()[0];
                                InternetAddress internetAddress = (InternetAddress) fromAddress;
                                String sender = internetAddress.getAddress();
                                if (MainController.listEmailString.contains(sender)) {
                                    System.out.println("Sender: " + sender);

                                    if (subject.contains("Shutdown")){
                                        if (emailFolder.isOpen())
                                            emailFolder.close(true);
                                    }
                                    boolean kq = MainClient.processRequest(subject, sender);
                                    if (kq) {
                                        System.out.println("Resolved " + subject);
                                    } else {
                                        System.out.println("Rejected " + subject);
                                    }
                                    first = false;
                                } else {
                                    System.out.println("Not accept sender" + sender);
                                }
                            }
                        }
                        if (emailFolder.isOpen())
                            emailFolder.close(true);
                    } catch (InterruptedException e) {
                        System.out.println("Can't connect to mail");
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    public static void main(String[] args) throws Exception {
        SendMail instance = SendMail.getInstance();
        CheckMail check = CheckMail.getInstance();
        check.listen();
    }
}


