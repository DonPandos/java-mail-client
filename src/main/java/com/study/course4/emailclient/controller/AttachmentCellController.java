package com.study.course4.emailclient.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.activation.DataSource;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AttachmentCellController implements Initializable {

    @FXML
    private Label filenameLabel;

    @FXML
    private Button downloadButton;

    DataSource dataSource;

    public AttachmentCellController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filenameLabel.setText(dataSource.getName());

        downloadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @SneakyThrows
            @Override
            public void handle(MouseEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(new Stage());
                File file = new File(selectedDirectory.getAbsolutePath() + "/" + dataSource.getName());
                FileUtils.copyInputStreamToFile(dataSource.getInputStream(), file);
            }
        });
    }
}
