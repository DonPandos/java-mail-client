package com.study.course4.emailclient.service;

import com.study.course4.emailclient.mail.Mail;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import java.io.IOException;

@Service
public class MailService {

    private final int MAILS_COUNT_ON_PAGE = 20;

    public Mail[] getMails(Store store, String folderName, int page) {
        try {
            Mail[] mails = new Mail[MAILS_COUNT_ON_PAGE];
            Folder folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            for(int i = (page - 1) * MAILS_COUNT_ON_PAGE; i < ((page - 1) + 1) * MAILS_COUNT_ON_PAGE; i++){
                Message message = messages[messages.length - 1 - i];
                Mail mail = new Mail();
                String from = message.getFrom()[0].toString().trim();
                String fromName = from.substring(0, from.lastIndexOf("<"));
                String fromEmail = from.substring(from.lastIndexOf("<") + 1, from.length() - 1);
                mail.setFromName(MimeUtility.decodeText(fromName));
                mail.setFromEmail(fromEmail);
                mail.setSubject(MimeUtility.decodeText(message.getSubject()));
                if(message.isMimeType("text/plain")) mail.setText(message.getContent().toString());
                if(message.isMimeType("text/html")) mail.setText(Jsoup.parse(message.getContent().toString()).text());
                mail.setDate(message.getSentDate());
                mails[i] = mail;
            }
            return mails;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
