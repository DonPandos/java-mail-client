package com.study.course4.emailclient;

import com.study.course4.emailclient.configuration.MailConfiguration;
import com.study.course4.emailclient.mail.EmailAuthenticator;
import com.study.course4.emailclient.mail.MailSession;
import com.sun.mail.pop3.POP3Store;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.util.Properties;

public class TestH {





    public static void main(String[] args) {
//        MailConfiguration.email = "ewrr_96@mail.ru";
//        MailConfiguration.password = "ch1nk1603ch1nk1603";
//        MailSender mailSender = MailConfiguration.getJavaMailSender();
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ewrr_96@mail.ru");
//        message.setTo("bogdant99@mail.ru");
//        message.setSubject("Hello");
//        message.setText("Login: ");
//        mailSender.send(message);


        //
//        Properties props = new Properties();
//        props.put("mail.debug", "false");
//        props.put("mail.store.protocol", "imaps");
//        props.put("mail.imap.ssl.enable", "true");
//        props.put("mail.imap.port", 993);
//
//        Authenticator auth = new EmailAuthenticator("ewrr_96@mail.ru", "ch1nk1603ch1nk1603");
//        Session session = Session.getDefaultInstance(props, auth);
//        session.setDebug(false);
//        try {
//            Store store = session.getStore();
//
//            store.connect("imap.mail.ru", "ewrr_96@mail.ru", "ch1nk1603ch1nk1603");
//
//            Folder inbox = store.getFolder("INBOX");
//            inbox.open(Folder.READ_ONLY);
//
//
//            Message[] messages = inbox.getMessages(); // refreshed
//
//            System.out.println(messages[messages.length - 1].getSubject());
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
    }
}
