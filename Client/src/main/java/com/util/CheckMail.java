package com.util;

import javax.mail.*;
import java.io.*;
import java.util.Properties;


public class CheckMail {

    private String username, password;
    private Store store;
    private Folder folder;
    public int count;
    private static CheckMail instance = new CheckMail();

    public static CheckMail getInstance() {
        return instance;
    }

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            this.username = br.readLine().trim();
            this.password = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CheckMail() {
        try {
            // create properties field
            getMail();
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties);
            this.store = emailSession.getStore("pop3s");
            store.connect("pop.gmail.com", this.username, this.password);
            this.folder = store.getFolder("INBOX");
            this.folder.open(Folder.READ_ONLY);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Message[] getAll() throws MessagingException {

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();
        emailFolder.close(false);
        return messages;
    }


    public static void writePart(Part p) throws Exception {
        if (p.isMimeType("text/plain")) {
            System.out.println((String) p.getContent());
        }
        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i));
        } else if (p.getContentType().contains("image/")) {
            String f = p.getFileName();
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f));
            BufferedInputStream input = new BufferedInputStream(p.getInputStream());
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
            }
        }

    }

    public void listen() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Folder emailFolder = null;
                        emailFolder = store.getFolder("INBOX");
                        emailFolder.open(Folder.READ_WRITE);
                        Message[] ls = emailFolder.getMessages();
                        for (Message m : ls) {
                            String subject = m.getSubject();
                            if (subject.startsWith("req")) {
                                MainClient.processRequest(subject);
                                m.setFlag(Flags.Flag.DELETED, true);
                            }
                        }
                        folder.close(true);
                    } catch (InterruptedException | MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
    }


    public void getResponse() throws Exception {
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);
        System.out.println(folder.getMessageCount());
        if (folder.getMessageCount() >= 1) {
            var a = emailFolder.getMessage(emailFolder.getMessageCount());
            writePart(a);
        } else {
            System.out.println("Empty to read");
        }
        emailFolder.close(false);
    }

    ;


    public static void main(String[] args) throws Exception {
        CheckMail check = CheckMail.getInstance();
        check.listen();
    }
}


