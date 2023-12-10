package com.util;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.*;

public class CheckMail {

    private String username, password;
    private Store store;
    private static final Properties properties = new Properties();
    private static CheckMail instance = new CheckMail();

    public static CheckMail getInstance() {
        return instance;
    }

    private CheckMail() {

        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3.host", "pop.gmail.com");
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

    }

    public String loadCredential() {
        try {
            SendMail intance = SendMail.getInstance();
            Session emailSession = Session.getInstance(properties);
            this.store = emailSession.getStore("pop3s");
            store.connect("pop.gmail.com", intance.email, intance.password);
        } catch (AuthenticationFailedException e) {
            SendMail.getInstance().logOut();
            return "You have not open the pop function on gmail";
        } catch (MessagingException e) {
            SendMail.getInstance().logOut();
            return "error happens";
        }
        return "OK";
    }

    public String listen(int id, int timesenconds) {
        Callable<String> task = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message m = messages[i];
                    System.out.println(m.getSubject());
                    String subject = m.getSubject();
                    if (subject.startsWith("res") && subject.contains(String.valueOf(id))) {
                        String a = null;
                        if (subject.contains("0000")) {
                            return "." + subject.split("/")[3].trim();
                        }
                        a = downloadAttachment(m);
                        folder.close(true);
                        if (a != null && !a.isBlank()) {
                            return a;
                        }
                        return null;
                    }
                }
            }
            return null;
        };

        var pool = Executors.newFixedThreadPool(1);
        var ans = pool.submit(task);
        String a = null;
        try {
            a = ans.get(timesenconds, TimeUnit.SECONDS);
            return a;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            ans.cancel(true);

        } finally {
            pool.shutdown();
        }
        return a;
    }


    public String downloadAttachment(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else {
            Multipart multiPart = (Multipart) content;
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String file = part.getFileName();
                    if (file.contains(".png")) {
                        Path folder = Path.of("cache");
                        if (!Files.exists(folder))
                            Files.createDirectory(folder);
                        BufferedImage image = ImageIO.read(part.getInputStream());
                        BufferedOutputStream out = new BufferedOutputStream(
                                Files.newOutputStream(folder.resolve(file)));
                        ImageIO.write(image, "png", out);
                        out.flush();
                        out.close();
                        return folder.resolve(file).toAbsolutePath().toString();
                    } else {
                        Path folder = Path.of("cache");
                        if (!Files.exists(folder))
                            Files.createDirectory(folder);
                        try (BufferedInputStream input = new BufferedInputStream(part.getInputStream());
                             BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(folder.resolve(file)))
                        ) {
                            byte[] buffer = new byte[32768];
                            int read;
                            while ((read = input.read(buffer, 0, 32768)) != -1) {
                                output.write(buffer, 0, read);
                                output.flush();
                            }
                            return folder.resolve(file).toAbsolutePath().toString();
                        }
                    }
                } else {
                    String contentType = part.getContentType();
                    if (contentType.startsWith("text")) {
                        String textContent = (String) part.getContent();
                        if (!textContent.contains("res") && !textContent.contains("req")) {
                            return textContent;
                        }
                    }

                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SendMail instance = SendMail.getInstance();
        System.out.println(instance.newCredential("huusangvtvip@gmail.com", "wuwcghrjkxwwfznn"));
        CheckMail check = CheckMail.getInstance();
        System.out.println(check.loadCredential());
        check.listen(100, 30);
    }
}


