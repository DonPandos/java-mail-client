package com.study.course4.emailclient.mail;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Mail {
    String fromName;
    String fromEmail;
    String subject;
    String text;
    Date date;
}
