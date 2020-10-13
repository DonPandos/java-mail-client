package com.study.course4.emailclient.mail;


import lombok.Data;

import javax.mail.*;
import java.util.Properties;

@Data
public class MailSession {

    private String email;
    private String password;

    public MailSession(){

    }

    public MailSession(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Store getStore() throws MessagingException {
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
        return store;
    }
}
