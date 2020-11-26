package com.study.course4.emailclient.configuration;

import com.study.course4.emailclient.mail.MailSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class MailConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Qualifier("new_session")
    public MailSession getNewMailSession(String email, String password) {
        String mailName = email.substring(email.lastIndexOf("@") + 1, email.length());
        Map<String, String> folderNames;
        switch(mailName) {
            case "mail.ru":
                folderNames = Stream.of(new Object[][] {
                        { "inbox", "inbox" },
                        { "sent", "Отправленные" },
                        { "drafts", "Черновики" },
                        { "trash", "Корзина" }
                }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
                break;
            default:
                folderNames = null;
                break;
        }
        MailSession mailSession = new MailSession(email, password, folderNames);
        return mailSession;
    }

}
