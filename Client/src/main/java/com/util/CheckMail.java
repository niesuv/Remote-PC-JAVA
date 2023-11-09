package com.util;

import javax.mail.*;
import java.io.*;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        for (int i = ls.length - 1; i >= 0; i--) {
                            var m = ls[i];
                            String subject = m.getSubject();
                            if (subject.startsWith("req")) {
                                boolean kq = MainClient.processRequest(subject);
                                m.setFlag(Flags.Flag.DELETED, true);
                                if (kq) {
                                    System.out.println("Resolved " + subject);
                                    break;
                                }
                                else {
                                    System.out.println("Rejected " + subject);
                                }


                            }
                        }
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
        CheckMail check = CheckMail.getInstance();
        check.listen();
    }
}


