package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("../resources/view/main_form.fxml")
public class MainFormController implements Initializable {
    ApplicationContext context;

    @Autowired
    MainFormController(ApplicationContext context){
        this.context = context;
    }

    @FXML
    JFXListView<String> foldersListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("12312");
        foldersListView.setItems(FXCollections.observableArrayList("123", "123"));
    }


}
