package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Folder;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MailListController implements Initializable {

    @FXML
    JFXListView<Mail> mailsListView;

    private ObservableList<Mail> mailObservableList;

    Folder folder;
    int page;

    MailService mailService;

    public MailListController(Folder folder, int page, MailService mailService) {
        this.folder = folder;
        this.page = page;
        this.mailService = mailService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INITIALIZE");
        mailObservableList = FXCollections.observableArrayList();

        mailsListView.setItems(mailObservableList);
        mailsListView.setCellFactory(mailListView -> new MailListViewCell());
        loadPage();
    }

    @SneakyThrows
    private void loadPage(){
        List<Mail> mails = mailService.getMails(folder, page);
        mailObservableList.clear();
        mailObservableList.addAll(mails);
    }
}
