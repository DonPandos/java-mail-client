package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Folder;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MailListController implements Initializable {

    @FXML
    private JFXListView<Mail> mailsListView;

    @FXML
    private AnchorPane mainPane;

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
        mailObservableList = FXCollections.observableArrayList();

        mailsListView.setItems(mailObservableList);
        mailsListView.setCellFactory(mailListView -> new MailListViewCell());
        loadPage();

        mailsListView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @SneakyThrows
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println(mailsListView.getSelectionModel().getSelectedItem().getFromEmail());
                Mail mail = mailsListView.getSelectionModel().getSelectedItem();
                mail.setContent(mailService.getMailContent(folder, mail.getNumber()));
                mail.setAttachments(mailService.getAttachmentFiles(folder, mail.getNumber()));
                MailPaneController mailPaneController = new MailPaneController(mail, mainPane);
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("../resources/view/mail_pane.fxml")
                );
                loader.setController(mailPaneController);
                mailsListView.setVisible(false);
                mainPane.getChildren().add(loader.load());
            }
        });
    }

    @SneakyThrows
    private void loadPage(){
        List<Mail> mails = mailService.getMails(folder, page);
        mailObservableList.clear();
        mailObservableList.addAll(mails);
    }

}
