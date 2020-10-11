package com.study.course4.emailclient.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("../resources/view/start_menu.fxml")
public class StartMenuController {

    private StringBuilder errors = new StringBuilder();

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
}
