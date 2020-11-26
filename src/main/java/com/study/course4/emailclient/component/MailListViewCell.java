package com.study.course4.emailclient.component;

import com.study.course4.emailclient.mail.Mail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class MailListViewCell extends ListCell<Mail> {
    @FXML
    private Circle letterCircle;

    @FXML
    private Label circleLetter;

    @FXML
    private Label fromNameLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane anchorPane;

    FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Mail mail, boolean empty) {
        super.updateItem(mail, empty);

        if(empty || mail == null){
            setText(null);
            setGraphic(null);
        } else {
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../resources/view/mail_list_cell.fxml"));
                fxmlLoader.setController(this);

                try{
                    fxmlLoader.load();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            Random rand = new Random();
            letterCircle.setFill(Color.rgb(rand.nextInt(255) + 1, rand.nextInt(255) + 1, rand.nextInt(255) + 1));
            if(mail.getFromName() != null && mail.getFromName().length() != 0) circleLetter.setText(String.valueOf(mail.getFromName().toUpperCase().charAt(0)));
            else circleLetter.setText(String.valueOf(mail.getFromEmail().toUpperCase().charAt(0)));
            fromNameLabel.setText(mail.getFromName());
            subjectLabel.setText(mail.getSubject());
            messageLabel.setText(mail.getContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'в' HH:mm:ss z");
            dateLabel.setText(mail.getDate()  == null ? "No info" : dateFormat.format(mail.getDate()));
        }

        setText(null);
        setGraphic(anchorPane);
    }
}
