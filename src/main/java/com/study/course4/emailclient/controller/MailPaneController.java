package com.study.course4.emailclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MailPaneController implements Initializable {

    @FXML
    private WebView webView;

    private String content;

    public MailPaneController(String content) {
        this.content = content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webView.getEngine().loadContent(content);
    }
}
