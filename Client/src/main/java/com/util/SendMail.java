package com.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SendMail {
    private Session session;
    private Message message;
    private String email, password;
    private static SendMail instance = new SendMail();

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            email = br.readLine().trim();
            password = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SendMail getInstance() {
        return instance;
    }

    private SendMail() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        getMail();
        this.session =
                Session.getDefaultInstance(
                        properties,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        email, password
                                );
                            }
                        }
                );

        try {
            this.message = new MimeMessage(this.session);

            this.message.setFrom(new InternetAddress(email));

            this.message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(this.email)
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMail(String subject, String text, String file) throws IOException, MessagingException {
        this.message.setSubject(subject);
        MimeBodyPart filePart = new MimeBodyPart();
        if (text != null)
            filePart.setText(text);
        if (file != null)
        {
            filePart.attachFile(file);
        }

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(filePart);
        this.message.setContent(multipart);
        Transport.send(message);

        if (file != null) {
            new Thread(() -> {
                try {
                    Files.deleteIfExists(Path.of(file));
                    System.out.println("Deleted File");
                } catch (IOException e) {
                    System.out.println("Cannot remove file after send");
                }
            }).start();
        }
        return;
    }


    public static void main(String[] args) {
        SendMail instance = SendMail.getInstance();
    }

}
