package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.util.LambdaSafe;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("../resources/view/main_form.fxml")
public class MainFormController implements Initializable {
    @FXML
    JFXListView<Mail> mailsListView;

    @FXML
    JFXListView<String> foldersListView;

    public static MailSession mailSession;

    ApplicationContext context;
    MailService mailService;

    @Autowired
    MainFormController(ApplicationContext context, MailService mailService){
        this.context = context;
        this.mailService = mailService;
    }

    private ObservableList<Mail> mailObservableList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPage("INBOX", 1);
        mailsListView.setItems(mailObservableList);
        mailsListView.setCellFactory(mailListView -> new MailListViewCell());

        foldersListView.setItems(FXCollections.observableArrayList("123", "123"));
    }

    @SneakyThrows
    private void loadPage(String folderName, int page){
        Mail[] mails = mailService.getMails(mailSession.getStore(), folderName, page);
        mailObservableList = FXCollections.observableArrayList();
        mailObservableList.addAll(mails);
    }

}
