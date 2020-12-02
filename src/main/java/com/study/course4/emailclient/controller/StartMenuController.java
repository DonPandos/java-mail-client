package com.study.course4.emailclient.controller;

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

import javax.mail.Store;

import java.net.URL;
import java.util.ResourceBundle;

import static com.study.course4.emailclient.controller.MainFormController.mailSession;


@Component
@FxmlView("../resources/view/start_menu.fxml")
public class StartMenuController implements Initializable {

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

        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        try {
            mailSession = context.getBean(MailSession.class, email, password);
            Stage stage = ((Stage) loginButton.getScene().getWindow());
            Scene scene = new Scene(fxWeaver.loadView(MainFormController.class));

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            errorLabel.setText("Неверный логин или пароль");
        }


    }

    @FXML
    public void handleOnCloseButtonClick(MouseEvent e){
        Platform.exit();
        System.exit(0);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.setText("kavun.bogdan16@gmail.com");
        passwordTextField.setText("tipeK440");
    }
}
