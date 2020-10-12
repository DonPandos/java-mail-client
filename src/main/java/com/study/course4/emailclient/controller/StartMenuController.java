package com.study.course4.emailclient.controller;


import com.study.course4.emailclient.TestH;
import com.study.course4.emailclient.mail.MailSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("../resources/view/start_menu.fxml")
public class StartMenuController{

    private StringBuilder errors = new StringBuilder();
    private final FxWeaver fxWeaver;
    private final ApplicationContext context;
    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    public void handleOnLoginButtonClick(MouseEvent event){

    }

    @Autowired
    public StartMenuController(FxWeaver fxWeaver, ApplicationContext context){
        this.fxWeaver = fxWeaver;
        this.context = context;
    }

    @FXML
    public void handleKeyTypedTextField(KeyEvent e){
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        errors = new StringBuilder();
        if(email.trim().isEmpty()) errors.append("Email не может быть пустым. \n");
        if(password.equals("")) errors.append("Пароль не может быть пустым. \n");
        if(!email.matches("^.*@(mail[.]ru|gmail[.]com|yandex[.]ru)$")) errors.append("Неподходящий email адрес. \n");
        if(errors.toString().equals("")) loginButton.setDisable(false);
        errorLabel.setText(errors.toString());
    }

    @FXML
    public void handleLoginButtonClicked(MouseEvent e){
        



        Stage stage = ((Stage)loginButton.getScene().getWindow());
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        Scene scene = new Scene(fxWeaver.loadView(MainFormController.class));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleOnCloseButtonClick(MouseEvent e){
        Platform.exit();
        System.exit(0);
    }



}
