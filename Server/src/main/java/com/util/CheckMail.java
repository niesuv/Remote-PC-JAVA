package com.util;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.*;

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

    public String listen(int id) {
        Callable<String> task = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                Message[] messages = folder.getMessages();
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message m = messages[i];
                    String subject = m.getSubject();
                    if (subject.startsWith("res") && subject.contains(String.valueOf(id))) {
                        String a = null;
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
            a = ans.get(40, TimeUnit.SECONDS);
            return a;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            ans.cancel(true);

        } finally {
            pool.shutdown();
        }
        return a;
    }


    public String downloadAttachment(Message message) throws MessagingException, IOException {
        Multipart multiPart = (Multipart) message.getContent();
        int numberOfParts = multiPart.getCount();
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                String file = part.getFileName();
                if (file.contains(".txt") || file.contains(".png")) {
                    Path folder = Path.of("cache");
                    if (!Files.exists(folder))
                        Files.createDirectory(folder);
                    try (BufferedInputStream input = new BufferedInputStream(part.getInputStream());
                         BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(folder.resolve(file)))
                    ) {
                        byte[] buffer = new byte[1024];
                        while (input.read(buffer, 0, 1024) != -1) {
                            output.write(buffer);
                            output.flush();
                        }
                        return folder.resolve(file).toAbsolutePath().toString();
                    }
                }
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        CheckMail check = CheckMail.getInstance();
        System.out.println(check.listen(1234));
    }
}


