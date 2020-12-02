package com.study.course4.emailclient.mail;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.activation.DataSource;
import java.io.File;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Mail {
    Integer number;
    String fromName;
    String fromEmail;
    String subject;
    String content;
    Date date;
    boolean seen;
    List<DataSource> attachments;
}
