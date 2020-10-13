package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import com.study.course4.emailclient.mail.MailSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.MimeUtility;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("../resources/view/main_form.fxml")
public class MainFormController implements Initializable {
    ApplicationContext context;

    public static MailSession mailSession;
    @Autowired
    MainFormController(ApplicationContext context){
        this.context = context;
    }

    @FXML
    JFXListView<String> foldersListView;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Folder folder = mailSession.getStore().getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();

        for(Message s : messages) System.out.println(MimeUtility.decodeText(s.getFrom()[0].toString()));
        foldersListView.setItems(FXCollections.observableArrayList("123", "123"));
    }

}
