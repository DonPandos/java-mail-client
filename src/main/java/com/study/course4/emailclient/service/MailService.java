package com.study.course4.emailclient.service;

import com.study.course4.emailclient.mail.Mail;
import lombok.SneakyThrows;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {

    private final int MAILS_COUNT_ON_PAGE = 20;

    @SneakyThrows
    public List<Mail> getMails(Folder folder, int page) {

        List<Mail> mails = new ArrayList<>();
        int messageStart = (page - 1) * MAILS_COUNT_ON_PAGE + 1;
        int messagesEnd = (page * MAILS_COUNT_ON_PAGE) - 1 > folder.getMessageCount() ? folder.getMessageCount() : (page * MAILS_COUNT_ON_PAGE) - 1;
        System.out.println(new Date());
//        List<MimeMessage> messages = Arrays.stream(folder.getMessages(page, messagesEnd)).map(message -> {
//            try {
//                return new MimeMessage((MimeMessage) message);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).collect(Collectors.toList()); // refreshed// }
//        List<Message> messages = Arrays.asList((folder.getMessages(1, messagesEnd)));
        System.out.println(new Date());
        for (int i = messageStart; i <= messagesEnd; i++) {
            Mail mail = new Mail();
            Message message = folder.getMessage(i);
            String from = message.getFrom()[0].toString().trim();
            try {
                String fromName = from.substring(0, from.lastIndexOf("<"));
                String fromEmail = from.substring(from.lastIndexOf("<") + 1, from.length() - 1);
                mail.setFromName(MimeUtility.decodeText(fromName));
                mail.setFromEmail(fromEmail);
            } catch(StringIndexOutOfBoundsException e) {
                mail.setFromEmail(from);
            }


            if (message.getSubject() != null) mail.setSubject(MimeUtility.decodeText(message.getSubject()));

//            try {
//                mail.setContent(new MimeMessageParser(message).parse().getHtmlContent());
//            } catch (Exception e) {
//                mail.setContent("");
//            }
            try {
                mail.setContent(message.getContent().toString());
            } catch (Exception e) {
                mail.setContent("");
            }
            mail.setDate(message.getSentDate());
            System.out.println(message.getSentDate());

            mails.add(mail);
        }

        return mails;

    }


}
