package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.MailService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;

import javax.mail.Folder;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MailListController implements Initializable {

    @FXML
    private JFXListView<Mail> mailsListView;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView nextPageButton;

    @FXML
    private ImageView prevPageButton;

    @FXML
    private JFXTextField pageTextField;

    private ObservableList<Mail> mailObservableList;

    Folder folder;
    Integer currentPage;
    Integer maxPage;

    MailService mailService;
    MailSession mailSession;
    AnchorPane majorPane;

    public MailListController(Folder folder, int page, MailService mailService, MailSession mailSession, AnchorPane majorPane) {
        this.folder = folder;
        this.currentPage = page;
        this.mailService = mailService;
        this.mailSession = mailSession;
        this.majorPane = majorPane;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mailObservableList = FXCollections.observableArrayList();
        maxPage = (int) Math.ceil((double) folder.getMessageCount() / 18.0);
        mailsListView.setItems(mailObservableList);
        mailsListView.setCellFactory(mailListView -> new MailListViewCell());
        loadPage();

        mailsListView.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @SneakyThrows
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                handleOnMailListViewClicked();
            }
        });

        nextPageButton.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                handleOnNextPageButtonClicked();
            }
        });

        prevPageButton.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                handleOnPrevPageButtonClicked();
            }
        });

        pageTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    currentPage = Integer.parseInt(pageTextField.getText());
                    loadPage();
                }

            }
        });
    }

    @SneakyThrows
    private void handleOnMailListViewClicked() {
        Mail mail = mailsListView.getSelectionModel().getSelectedItem();
        mail.setContent(mailService.getMailContent(folder, mail.getNumber()));
        mail.setAttachments(mailService.getAttachmentFiles(folder, mail.getNumber()));

        if (folder.getName().equals("Черновики")) {
            WriteMailFormController writeMailFormController = new WriteMailFormController(
                    mailService.getReplyTo(folder, mail.getNumber()),
                    mail.getSubject(),
                    mail.getContent(),
                    majorPane,
                    mailSession,
                    mailService,
                    mail.getAttachments()
            );
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../resources/view/write_mail_form.fxml")
            );
            loader.setController(writeMailFormController);
            majorPane.getChildren().remove(mainPane);
            majorPane.getChildren().add(loader.load());
        } else {

            MailPaneController mailPaneController = new MailPaneController(mail, mainPane);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../resources/view/mail_pane.fxml")
            );
            loader.setController(mailPaneController);
            mailsListView.setVisible(false);
            mainPane.getChildren().add(loader.load());
        }
    }

    private void handleOnPrevPageButtonClicked() {
        currentPage--;
        pageTextField.setText(currentPage.toString());
        loadPage();
    }

    private void handleOnNextPageButtonClicked() {
        currentPage++;
        pageTextField.setText(currentPage.toString());
        loadPage();
    }


    @SneakyThrows
    private void loadPage() {
        if (currentPage == 1) prevPageButton.setVisible(false);
        else prevPageButton.setVisible(true);
        if (currentPage == maxPage) nextPageButton.setVisible(false);
        else nextPageButton.setVisible(true);

        List<Mail> mails = mailService.getMails(folder, currentPage);
        mailObservableList.clear();
        mailObservableList.addAll(mails);
    }

}
