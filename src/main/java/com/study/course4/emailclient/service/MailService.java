package com.study.course4.emailclient.service;

import com.study.course4.emailclient.mail.Mail;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.ArrayList;

import java.util.List;

@Service
public class MailService {

    private final int MAILS_COUNT_ON_PAGE = 20;

    @SneakyThrows
    public List<Mail> getMails(Folder folder, int page) {

        List<Mail> mails = new ArrayList<>();
        int messageStart = (page - 1) * MAILS_COUNT_ON_PAGE + 1;
        int messagesEnd = (page * MAILS_COUNT_ON_PAGE) - 1 > folder.getMessageCount() ? folder.getMessageCount() : (page * MAILS_COUNT_ON_PAGE) - 1;
        int messageCount = folder.getMessageCount();
        for (int i = messageCount - messageStart + 1; i >= messageCount - messagesEnd + 1; i--) {
            Mail mail = new Mail();
            MimeMessage message = (MimeMessage) folder.getMessage(i);
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
            else mail.setSubject("No subject");
            mail.setSeen(message.getFlags().contains(Flags.Flag.SEEN));
            mail.setDate(message.getSentDate());
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


}
