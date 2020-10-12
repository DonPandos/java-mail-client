package com.study.course4.emailclient.configuration;

import com.study.course4.emailclient.mail.MailSession;
import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class MailConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public MailSession getMailSession(String email, String password) {
        MailSession mailSession = new MailSession(email, password);
        return mailSession;
    }
}
