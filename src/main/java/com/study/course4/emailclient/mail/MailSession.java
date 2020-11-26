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
    private Map<String, Folder> folders;
    private Store store;
    public MailSession(){

    }

    @SneakyThrows
    public MailSession(String email, String password, Map<String, String> folderNames) {
        this.email = email;
        this.password = password;
        this.folderNames = folderNames;

        Properties props = new Properties();
        props.put("mail.debug", "false");
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.port", 993);

        Authenticator auth = new EmailAuthenticator(email, password);
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(false);
        Store store = session.getStore();
        store.connect("imap.mail.ru", email, password);

        folders = new HashMap<>();
        folders.put("inbox", store.getFolder(folderNames.get("inbox")));
        folders.put("sent", store.getFolder(folderNames.get("sent")));
        folders.put("trash", store.getFolder(folderNames.get("trash")));
        folders.put("drafts", store.getFolder(folderNames.get("drafts")));
        for(Folder folder : folders.values()) {
            folder.open(Folder.READ_ONLY);
        }
    }

    public Folder getFolder(String folderName) {
        return folders.get(folderName);
    }
}
