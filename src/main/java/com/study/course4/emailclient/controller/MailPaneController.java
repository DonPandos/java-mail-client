package com.study.course4.emailclient.controller;

import com.study.course4.emailclient.mail.Mail;
import com.study.course4.emailclient.service.CryptService;
import com.study.course4.emailclient.service.SignService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import lombok.SneakyThrows;

import javax.activation.DataSource;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MailPaneController implements Initializable {

    @FXML
    private Circle letterCircle;

    @FXML
    private Label circleLetter;

    @FXML
    private Label fromNameLabel;

    @FXML
    private Label fromEmailLabel;

    @FXML
    private Label sentDateLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private WebView contentWebView;

    @FXML
    private GridPane atachmentsPane;

    @FXML
    private ImageView backButton;

    private Mail mail;
    private AnchorPane mainPane;
    private boolean isSigned;
    private boolean sign;
    public MailPaneController(Mail mail, AnchorPane mainPane) {
        this.mail = mail;
        this.mainPane = mainPane;
        this.isSigned = false;
        for(DataSource attachment : mail.getAttachments()) {
            if(attachment.getName().equals("encdeskey")) {
                Pair<String, List<DataSource>> pair = CryptService.decrypt(mail.getContent(), mail.getAttachments(), this.mail.getFromEmail());
                this.mail.setContent(pair.getKey());
                this.mail.setAttachments(pair.getValue());
                break;
            }
        }
        for(DataSource attachment : mail.getAttachments()) {
            if(attachment.getName().equals("pubandsign")){
                this.isSigned = true;
                this.sign = SignService.checkSign(mail.getContent(), mail.getAttachments());
                break;
            }
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Random rand = new Random();
        letterCircle.setFill(Color.rgb(rand.nextInt(255) + 1, rand.nextInt(255) + 1, rand.nextInt(255) + 1));
        if(mail.getFromName() != null && mail.getFromName().length() != 0) circleLetter.setText(String.valueOf(mail.getFromName().toUpperCase().charAt(0)));
        else circleLetter.setText(String.valueOf(mail.getFromEmail().toUpperCase().charAt(0)));
        fromNameLabel.setText(mail.getFromName());
        subjectLabel.setText(mail.getSubject());
        fromEmailLabel.setText(mail.getFromEmail());
        contentWebView.getEngine().loadContent(mail.getContent());
        if(isSigned) {
            if(sign) errorLabel.setText("Письмо имеет ЭЦП. ЭЦП валидна.");
            else errorLabel.setText("Письмо имеет ЭЦП. ЭЦП не валидна.");
        }
        int counter = 1;
        for(DataSource dataSource : mail.getAttachments()) {
            AttachmentCellController attachmentCellController = new AttachmentCellController(dataSource);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../resources/view/attachment_cell.fxml")
            );
            loader.setController(attachmentCellController);
            atachmentsPane.add(loader.load(), 1, counter);
            counter++;
        }

        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainPane.getChildren().remove(contentWebView.getParent());
                mainPane.getChildren().get(0).setVisible(true);
                mainPane.getChildren().get(0).requestFocus();
            }
        });
    }
}
