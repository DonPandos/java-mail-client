package com.study.course4.emailclient.controller;

import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.AccountService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.mail.Store;

import java.awt.print.Paper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

import static com.study.course4.emailclient.controller.MainFormController.currentAccount;
import static com.study.course4.emailclient.controller.MainFormController.mailSession;
import static com.study.course4.emailclient.controller.MainFormController.mailSessions;


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

    @FXML
    private Button addAccountButton;

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
            List<String> accounts = AccountService.getAccounts();
            boolean exists = false;
            for(String account : accounts) {
                if(account.substring(0, account.indexOf(" ")).equals(email)) {
                    System.out.println("equals");
                    exists = true;
                }
            }
            if(!exists) {
                FileWriter fw = new FileWriter("src/main/java/com/study/course4/emailclient/files/accounts.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(email + " " + password);
                bw.newLine();
                bw.flush();
                bw.close();
                mailSessions.put(email + " " + password, mailSession);
            }
            currentAccount = email + " " + password;
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


    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
