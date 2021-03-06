package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.study.course4.emailclient.crypt.RSACrypt;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.CryptService;
import com.study.course4.emailclient.service.MailService;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.SneakyThrows;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.study.course4.emailclient.controller.MainFormController.mailSession;

public class WriteMailFormController implements Initializable {

    @FXML
    private HTMLEditor contentEditor;

    @FXML
    private JFXTextField toEmailTextField;

    @FXML
    private JFXTextField subjectTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView backButton;

    @FXML
    private ImageView addAttachmentButton;

    @FXML
    private ListView listView;

    @FXML
    private FlowPane attachmentPane;

    @FXML
    private JFXCheckBox cryptCheckBox;

    @FXML
    private JFXCheckBox signCheckBox;

    @FXML
    private JFXTextArea pubKeyTextArea;

    private AnchorPane mainPane;
    private MailService mailService;
    private List<DataSource> attachments;
    private String toEmail;
    private String subject;
    private String content;



    public WriteMailFormController(AnchorPane mainPane, MailSession mailSession, MailService mailService) {
        this.mainPane = mainPane;
        this.mailService = mailService;
        attachments = new ArrayList<>();
    }

    public WriteMailFormController(String toEmail, String subject, String content, AnchorPane mainPane, MailSession mailSession, MailService mailService, List<DataSource> attachments) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
        this.mainPane = mainPane;
        this.mailService = mailService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(toEmail != null) {
            toEmailTextField.setText(toEmail);
            subjectTextField.setText(subject);
            contentEditor.setHtmlText(content);
            for(DataSource dataSource : attachments) {
                AttachmentCellWriteEmailController attachmentCellWriteEmailController = new AttachmentCellWriteEmailController(dataSource.getName(), attachments, attachmentPane, addAttachmentButton);
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("../resources/view/attachment_cell_write_email.fxml")
                );
                loader.setController(attachmentCellWriteEmailController);
                attachmentPane.getChildren().add(loader.load());
            }
        }
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOnBackButtonClick();
            }
        });

        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOnSendEmailButtonClick();
            }
        });

        addAttachmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOnAddAttachmentButtonClick();
            }
        });

        cryptCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOnCryptCheckBoxClick();
            }
        });
    }

    public void handleOnBackButtonClick() {
        mainPane.getChildren().remove(backButton.getParent());
        mainPane.getChildren().get(0).setVisible(true);
    }

    public void handleOnSendEmailButtonClick() {
        StringBuilder errors = new StringBuilder("");

        String toEmail = toEmailTextField.getText();
        String subject = subjectTextField.getText();
        String htmlContent = contentEditor.getHtmlText();

        if(!toEmail.matches("^.*@(mail[.]ru|gmail[.]com|yandex[.]ru)$")) errors.append("Неподходящий email адрес. \n");

        if(!errors.toString().equals("")) {
            errorLabel.setText(errors.toString());
            return;
        }

        if(signCheckBox.isSelected()) {
            SignService.createSign(htmlContent, attachments);
        }

        if(cryptCheckBox.isSelected()) {
            String rsaPubKeyString = pubKeyTextArea.getText();
            if(!RSACrypt.isPublicRsaKey(rsaPubKeyString)) {
                errorLabel.setText("Неверный RSA ключ");
                return;
            }

            Pair<String, List<DataSource>> pair = CryptService.encrypt(htmlContent, attachments, rsaPubKeyString);
            boolean result = mailService.sendEmail(toEmail, subject, pair.getKey(), pair.getValue());
            if(result) errorLabel.setText("Письмо успешно доставлено!");
            else errorLabel.setText("При отправке письма возникли ошибки.");
            return;
        }

        boolean result = mailService.sendEmail(toEmail, subject, htmlContent, attachments);
        if(result) errorLabel.setText("Письмо успешно доставлено!");
        else errorLabel.setText("При отправке письма возникли ошибки.");
    }

    @SneakyThrows
    public void handleOnAddAttachmentButtonClick() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        attachments.add(new FileDataSource(file));
        AttachmentCellWriteEmailController attachmentCellWriteEmailController = new AttachmentCellWriteEmailController(file.getName(), attachments, attachmentPane, addAttachmentButton);
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../resources/view/attachment_cell_write_email.fxml")
        );
        loader.setController(attachmentCellWriteEmailController);
            attachmentPane.getChildren().add(loader.load());
    }

    public void handleOnCryptCheckBoxClick() {
        boolean value = cryptCheckBox.isSelected();
        if(value) {
            pubKeyTextArea.setDisable(false);
        } else {
            pubKeyTextArea.setDisable(true);
        }
    }
}
