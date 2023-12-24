package com.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

public class SendMail {
    public boolean logIn;
    private Session session;
    private Message message;
    public String email, password;
    private static final String toMail = "ailearning.hcmus@gmail.com";
    private static final Properties properties = new Properties();
    private static SendMail instance = new SendMail();

    public static SendMail getInstance() {
        return instance;
    }

    private SendMail() {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }

    /**
     * Kiểm tra thông tin đăng nhập
     *
     * @param _email
     * @param _password
     * @return "ok" nếu đăng nhập thành công, "Bad Credebtials" nếu thất bại
     */
    public String newCredential(String _email, String _password) {
        this.email = _email;
        this.password = _password;
        session =
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
            session.getTransport().connect();
            session.getTransport().close();
            this.message = new MimeMessage(this.session);

            this.message.setFrom(new InternetAddress(email));

            this.message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(toMail)
            );
            logIn = true;
            return "OK";
        } catch (AuthenticationFailedException e) {
            return "Bad Credential";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Some error happens";
        }
    }

    public void logOut() {
        logIn = false;
        session = null;
    }


    public void sendMail(String subject) throws IOException, MessagingException {

        this.message.setSubject(subject);
        MimeBodyPart filePart = new MimeBodyPart();
        filePart.setText("Remote PC request");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(filePart);
        this.message.setContent(multipart);
        Transport.send(message);
        System.out.println("Send mail file success!");
    }


    public static void main(String[] args) {
        SendMail instance = SendMail.getInstance();
        System.out.println(instance.newCredential("ailearning.hcmus@gmail.com", "trlaovprldjidund"));
        System.out.println(instance.newCredential("ailearning.hcmus@gmail.com", "trlaovprldjidunds"));

    }

    public String getGmailAccountName() {
        return email;
    }


}
