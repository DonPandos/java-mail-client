package com.study.course4.emailclient.service;

import com.study.course4.emailclient.mail.EmailAuthenticator;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.mail.MailSession;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;

import static com.study.course4.emailclient.controller.MainFormController.mailSession;
import static com.study.course4.emailclient.controller.MainFormController.mailSessions;

@Service
public class MailService {

    private final int MAILS_COUNT_ON_PAGE = 18;

    @SneakyThrows
    public List<Mail> getMails(Folder folder, int page) {

        List<Mail> mails = new ArrayList<>();
        int messageStart = (page - 1) * MAILS_COUNT_ON_PAGE + 1;
        int messagesEnd = (page * MAILS_COUNT_ON_PAGE) - 1 > folder.getMessageCount() ? folder.getMessageCount() : (page * MAILS_COUNT_ON_PAGE) - 1;
        int messageCount = folder.getMessageCount();
        for (int i = messageCount - messageStart + 1; i >= messageCount - messagesEnd + 1; i--) {
            Mail mail = new Mail();
            Message message = folder.getMessage(i);
            Address[] from = message.getFrom();
            mail.setFromName(((InternetAddress) from[0]).getPersonal());
            mail.setFromEmail(((InternetAddress) from[0]).getAddress());
            if (message.getSubject() != null) mail.setSubject(MimeUtility.decodeText(message.getSubject()));
            else mail.setSubject("No subject");
            mail.setDate(message.getSentDate());
            mail.setSeen(true);
            mail.setNumber(i);
            mails.add(mail);
        }

        return mails;

    }

    @SneakyThrows
    public String getMailContent(Folder folder, Integer mailNumber) {
        MimeMessage message = (MimeMessage) folder.getMessage(mailNumber);
        MimeMessageParser parser = new MimeMessageParser(message).parse();
        return parser.getHtmlContent();
    }

    @SneakyThrows
    public List<DataSource> getAttachmentFiles(Folder folder, Integer mailNumber) {
        MimeMessage message = (MimeMessage) folder.getMessage(mailNumber);
        MimeMessageParser parser = new MimeMessageParser(message).parse();
        return parser.getAttachmentList();
    }

    @SneakyThrows
    public String getReplyTo(Folder folder, Integer mailNumber) {
        MimeMessage message = (MimeMessage) folder.getMessage(mailNumber);
        return ((InternetAddress) message.getReplyTo()[0]).getAddress();
    }

    public boolean sendEmail(String toEmail, String subject, String htmlContent, List<DataSource> dataSources) {
        try {
            Message message = new MimeMessage(mailSession.getSession());
            message.setFrom(new InternetAddress(mailSession.getEmail(), mailSession.getName()));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            for(DataSource dataSource : dataSources) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(dataSource));
                messageBodyPart.setFileName(dataSource.getName());
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart);
            Transport.send(message, mailSession.getEmail(), mailSession.getPassword());
            //Transport.send(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
