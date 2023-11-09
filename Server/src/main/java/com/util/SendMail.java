package com.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SendMail {
    private Session session;
    private Message message;
    private String yourmail, password;
    private static SendMail instance = new SendMail();

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            this.yourmail = br.readLine().trim();
            this.password = br.readLine().trim();
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
                                        yourmail, password
                                );
                            }
                        }
                );

        try {
            this.message = new MimeMessage(this.session);

            this.message.setFrom(new InternetAddress(yourmail));

            this.message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(this.yourmail)
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
            filePart.attachFile(file);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(filePart);
        this.message.setContent(multipart);
        Transport.send(message);
        System.out.println("Send mail file success!");
    }


    public static void main(String[] args) {
        SendMail instance = SendMail.getInstance();
    }

}
