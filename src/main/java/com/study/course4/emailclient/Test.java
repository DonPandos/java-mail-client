package com.study.course4.emailclient;

import com.study.course4.emailclient.configuration.MailConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class Test {

    public static void main(String[] args) {
        MailConfiguration.email = "ewrr_96@mail.ru";
        MailConfiguration.password = "ch1nk1603ch1nk1603";
        MailSender mailSender = MailConfiguration.getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ewrr_96@mail.ru");
        message.setTo("bogdant99@mail.ru");
        message.setSubject("Hello");
        message.setText("Login: ");
        mailSender.send(message);
    }
}
