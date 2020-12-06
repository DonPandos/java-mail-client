package com.study.course4.emailclient;

import com.study.course4.emailclient.configuration.MailConfiguration;
import com.study.course4.emailclient.mail.EmailAuthenticator;
import com.study.course4.emailclient.mail.MailSession;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Store;
import lombok.SneakyThrows;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class TestH {


    @SneakyThrows
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


        Properties props = new Properties();
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imaps.connectiontimeout", "5000");
        props.setProperty("mail.imaps.timeout", "5000");

        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Authenticator auth = new EmailAuthenticator("kavun.bogdan16@gmail.com", "tipeK440");
        Session session = Session.getDefaultInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("kavun.bogdan16@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse("ewrr_96@mail.ru")
            );
            message.setSubject("Subj");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("Htmlcontent", "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        session.setDebug(false);
//        Store store = session.getStore("imaps");
//        store.connect("imap.gmail.com", "aleksey.borovikov.11@gmail.com", "sup1403f,");
//        Folder folderSent = store.getFolder("[Gmail]/Отправленные");
//        folderSent.open(Folder.READ_ONLY);
//        for (Folder folder : store.getDefaultFolder().list()) {
//            System.out.println("---" + folder.getName());
//            if(folder.getName().equals("[Gmail]")) {
//                for(Folder folder1 : folder.list()) {
//                    System.out.println(folder1.getName());
//                }
//            }
//            String[] attributes = ((IMAPFolder) folder).getAttributes();
//            for (String attr : attributes) {
//                System.out.println(attr);
//            }
//        }
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
//
//            Folder inbox = store.getFolder("inbox");
//            Folder sent = store.getFolder("Отправленные");
//            sent.open(Folder.READ_ONLY);
//
//            System.out.println(new Date());
////            List<MimeMessage> messages = Arrays.stream(sent.getMessages(1, 20)).map(message -> {
////                try {
////                    return new MimeMessage((MimeMessage) message);
////                } catch (MessagingException e) {
////                    e.printStackTrace();
////                }
////                return null;
////            }).collect(Collectors.toList()); // refreshed
//            //List<Message> messages = Arrays.asList((sent.getMessages(1, 20)));
//            for(int i = 1; i < 20; i++) {
//                MimeMessage mimeMessage = (MimeMessage) sent.getMessage(i);
//            }
//            System.out.println(new Date());
////            for (MimeMessage message : messages) {
////                System.out.println(message.getContentType());
////                MimeMessageParser mimeMessageParser = new MimeMessageParser(message).parse();
////                System.out.println(mimeMessageParser.getHtmlContent());
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
