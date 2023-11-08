package com.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ReceiveEmail {
    public static void main(String[] args) {
        final String username = "urmail@gmail.com";
        final String password = "urpass";

        Properties props = new Properties();
        props.put("mail.imap.host", "imap.gmail.com");
        props.put("mail.imap.port", "25");
        props.put("mail.imap.starttls.enable", "true");

        Session session = Session.getInstance(props, null);

        try {
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();

            for (Message message : messages) {
                String contentType = message.getContentType();
                if (contentType.contains("multipart")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            InputStream inputStream = bodyPart.getInputStream();
                            File file = new File(bodyPart.getFileName());
                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] buf = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buf)) != -1) {
                                fos.write(buf, 0, bytesRead);
                            }
                            fos.close();
                        }
                    }
                }
            }
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
