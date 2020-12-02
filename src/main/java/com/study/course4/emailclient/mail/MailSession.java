package com.study.course4.emailclient.mail;


import lombok.Data;
import lombok.SneakyThrows;

import javax.mail.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Data
public class MailSession {

    private String email;
    private String password;
    private Map<String, String> folderNames;
    private Store store;
    public MailSession(){

    }

    @SneakyThrows
    public MailSession(String email, String password, Map<String, String> folderNames, String host) {
        this.email = email;
        this.password = password;
        this.folderNames = folderNames;

        Properties props = new Properties();
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", "5000");
        props.setProperty("mail.imaps.timeout", "5000");

        Authenticator auth = new EmailAuthenticator(email, password);
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(false);
        store = session.getStore("imaps");
        store.connect(host, email, password);

    }

    @SneakyThrows
    public Folder getFolder(String folderName) {
        if (!store.isConnected()) {
            store.connect();
        }
        System.out.println(folderNames.get(folderName));
        Folder folder = store.getFolder(folderNames.get(folderName));
        folder.open(Folder.READ_ONLY);
        return folder;
    }
}
