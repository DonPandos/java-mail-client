package com.study.course4.emailclient.mail;


import lombok.Data;
import lombok.SneakyThrows;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class MailSession {

    private String email;
    private String password;
    private Map<String, String> folderNames;
    private Store store;
    private Session session;
    private List<Folder> foldersList;
    private String name;
    private String host;
    public MailSession(){
        this.email = null;
    }

    @SneakyThrows
    public String getName() {
        if(name == null) {
            name = ((InternetAddress) foldersList.get(1).getMessage(1).getFrom()[0]).getPersonal();
        }
        return name;
    }

    @SneakyThrows
    public MailSession(String email, String password, Map<String, String> folderNames, String host) {
        this.email = email;
        this.password = password;
        this.folderNames = folderNames;
        this.host = host;
        System.out.println(email + "|");
        System.out.println(password + "|");

        Properties props = new Properties();
        //imap
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", "5000");
        props.setProperty("mail.imaps.timeout", "5000");
        //smtp
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", "smtp." + host);
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        Authenticator auth = new EmailAuthenticator(email, password);
        session = Session.getDefaultInstance(props, auth);
        store = session.getStore("imaps");
        store.connect("imap." + host , email, password);

        foldersList = Stream.of(
                store.getFolder(folderNames.get("inbox")),
                store.getFolder(folderNames.get("sent")),
                store.getFolder(folderNames.get("trash")),
                store.getFolder(folderNames.get("drafts"))
        ).collect(Collectors.toList());

        List<Thread> threads = new ArrayList<>();

        for(Folder folder : foldersList) {
            Thread thread = new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    folder.open(Folder.READ_ONLY);
                }
            });
            threads.add(thread);
            thread.start();
        }
        //threads.get(0).join();

    }

    @SneakyThrows
    public Folder getFolder(String folderName) {
        if (!store.isConnected()) {
            store.connect();

            foldersList = Stream.of(
                    store.getFolder(folderNames.get("inbox")),
                    store.getFolder(folderNames.get("sent")),
                    store.getFolder(folderNames.get("trash")),
                    store.getFolder(folderNames.get("drafts"))
            ).collect(Collectors.toList());

            for(Folder folder : foldersList) folder.open(Folder.READ_ONLY);
        }
        return foldersList.stream()
                .filter(folder -> folder.getFullName().equals(folderNames.get(folderName)))
                .findAny()
                .get();
    }
}
