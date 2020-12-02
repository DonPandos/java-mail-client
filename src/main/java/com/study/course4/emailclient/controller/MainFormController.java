package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.study.course4.emailclient.component.FolderListViewCell;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.pojo.FolderPojo;
import com.study.course4.emailclient.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;

import javax.mail.Folder;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
@FxmlView("../resources/view/main_form.fxml")
public class MainFormController implements Initializable {

    @FXML
    JFXListView<FolderPojo> foldersListView;

    @FXML
    Pane mainPane;

    public static MailSession mailSession;

    ApplicationContext context;
    MailService mailService;

    @Autowired
    MainFormController(ApplicationContext context, MailService mailService) {
        this.context = context;
        this.mailService = mailService;
    }

    private ObservableList<FolderPojo> folderObservableList;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadFolders();

        foldersListView.setItems(folderObservableList);
        foldersListView.setCellFactory(folderListView -> new FolderListViewCell());

        MailListController mailListController = new MailListController(mailSession.getFolder("inbox"), 1, mailService);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../resources/view/mail_list.fxml")
        );
        loader.setController(mailListController);
        mainPane.getChildren().add(loader.load());

    }

    private void loadFolders() {
        FolderPojo[] folders = new FolderPojo[4];
        folders[0] = new FolderPojo("Входящие", "inbox-folder.png");
        folders[1] = new FolderPojo("Отправленные", "outbox-folder.png");
        folders[2] = new FolderPojo("Черновики", "draft-folder.png");
        folders[3] = new FolderPojo("Корзина", "trash-folder.png");
        folderObservableList = FXCollections.observableArrayList();
        folderObservableList.addAll(folders);
    }

    @SneakyThrows
    @FXML
    public void handleFoldersListViewClick(MouseEvent event) {
        String folderName = "";
        switch (foldersListView.getSelectionModel().getSelectedItem().getFolderName()) {
            case "Входящие":
                folderName = "inbox";
                break;
            case "Отправленные":
                folderName = "sent";
                break;
            case "Черновики":
                folderName = "drafts";
                break;
            case "Корзина":
                folderName = "trash";
                break;
        }

        MailListController mailListController = new MailListController(mailSession.getFolder(folderName), 1, mailService);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../resources/view/mail_list.fxml")
        );
        loader.setController(mailListController);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(loader.load());
    }
}
