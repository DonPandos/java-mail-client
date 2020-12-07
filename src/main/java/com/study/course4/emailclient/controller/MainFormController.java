package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.study.course4.emailclient.component.AccountsListViewCell;
import com.study.course4.emailclient.component.FolderListViewCell;
import com.study.course4.emailclient.component.MailListViewCell;
import com.study.course4.emailclient.crypt.RSACrypt;
import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.pojo.FolderPojo;
import com.study.course4.emailclient.service.AccountService;
import com.study.course4.emailclient.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;

import javax.mail.Folder;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.Map.Entry;

import static com.study.course4.emailclient.controller.MainFormController.mailSession;

@Component
@FxmlView("../resources/view/main_form.fxml")
public class MainFormController implements Initializable {

    @FXML
    JFXListView<FolderPojo> foldersListView;

    @FXML
    JFXListView<String> accountsListView;

    @FXML
    JFXButton generateKeyButton;

    @FXML
    JFXTextField toEmailKeyTextField;

    @FXML
    JFXTextArea keyGenerateTextArea;

    @FXML
    AnchorPane mainPane;

    @FXML
    private Button sendEmailButton;

    public static MailSession mailSession;
    public static Map<String, MailSession> mailSessions;
    public static String currentAccount;

    ApplicationContext context;
    MailService mailService;

    @Autowired
    MainFormController(ApplicationContext context, MailService mailService) {
        this.context = context;
        this.mailService = mailService;
    }

    private ObservableList<FolderPojo> folderObservableList;
    private ObservableList<String> accountsObservableList;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFolders();
        loadAccounts();

        foldersListView.setItems(folderObservableList);
        foldersListView.setCellFactory(folderListView -> new FolderListViewCell());

        accountsListView.setItems(accountsObservableList);
        accountsListView.setCellFactory(accountsListView -> new AccountsListViewCell());

        for(Map.Entry entry : mailSessions.entrySet()) {
            if (mailSession == entry.getValue()) accountsListView.getSelectionModel().select((String) entry.getKey());
        }

        MailListController mailListController = new MailListController(mailSession.getFolder("inbox"), 1, mailService, mailSession, mainPane);
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
    private void loadAccounts() {
        List<String> accounts = Files.readAllLines(new File("src/main/java/com/study/course4/emailclient/files/accounts.txt").toPath());
        accountsObservableList = FXCollections.observableArrayList();
        accountsObservableList.addAll(accounts);
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

        MailListController mailListController = new MailListController(mailSession.getFolder(folderName), 1, mailService, mailSession, mainPane);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../resources/view/mail_list.fxml")
        );
        loader.setController(mailListController);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(loader.load());
    }

    @SneakyThrows
    @FXML
    public void handleOnAccountsListViewClick(MouseEvent event) {
        String accountClicked = accountsListView.getSelectionModel().getSelectedItem();
        if(!accountClicked.equals(currentAccount)) {
            currentAccount = accountClicked;
            MailSession mailSession;
            if(mailSessions.get(currentAccount) == null) {
               mailSession = context.getBean(MailSession.class,
                       currentAccount.substring(0, currentAccount.indexOf(" ")),
                       currentAccount.substring(currentAccount.indexOf(" ") + 1, currentAccount.length()));
               mailSessions.replace(currentAccount, mailSession);
            } else {
                mailSession = mailSessions.get(accountClicked);
            }
            this.mailSession = mailSessions.get(currentAccount);
            MailListController mailListController = new MailListController(mailSession.getFolder("inbox"), 1, mailService, mailSession, mainPane);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../resources/view/mail_list.fxml")
            );
            loader.setController(mailListController);
            mainPane.getChildren().remove(0, mainPane.getChildren().size());
            mainPane.getChildren().add(loader.load());
        }
    }

    @SneakyThrows
    @FXML
    public void handleOnEmailSendButtonClick(MouseEvent event) {
        WriteMailFormController writeMailFormController = new WriteMailFormController(mainPane, mailSession, mailService);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../resources/view/write_mail_form.fxml")
        );
        loader.setController(writeMailFormController);
        mainPane.getChildren().get(0).setVisible(false);
        mainPane.getChildren().add(loader.load());
    }

    @FXML
    public void handleOnAddAccountButtonClick(MouseEvent e) {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(StartMenuController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        mainPane.getScene().getWindow().hide();
        stage.show();

    }

    @SneakyThrows
    @FXML
    public void handleOnDeleteAccountButtonClick(MouseEvent e) {
        String accountToDelete = accountsListView.getSelectionModel().getSelectedItem();
        AccountService.deleteAccount(accountToDelete);
        accountsObservableList.remove(accountToDelete);
        accountsListView.refresh();
        mailSessions.remove(accountToDelete);
        if(mailSessions.size() == 0) {
            FxWeaver fxWeaver = context.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(StartMenuController.class);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            mainPane.getScene().getWindow().hide();
            stage.show();
        } else {
            Entry entry = mailSessions.entrySet().iterator().next();
            currentAccount = (String) entry.getKey();
            mailSession = (MailSession) entry.getValue();
            MailListController mailListController = new MailListController(mailSession.getFolder("inbox"), 1, mailService, mailSession, mainPane);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../resources/view/mail_list.fxml")
            );
            loader.setController(mailListController);
            mainPane.getChildren().removeAll();
            mainPane.getChildren().add(loader.load());
        }
    }

    @FXML
    public void onKeyGenerateButtonClick() {
        String email = toEmailKeyTextField.getText();
        if(!email.matches("^.*@(mail[.]ru|gmail[.]com|yandex[.]ru)$")) {
            keyGenerateTextArea.setText("Неподходящий email адрес. \n");
            return;
        }
        keyGenerateTextArea.setText(RSACrypt.generatePair(mailSession.getEmail(), email));

    }
}
