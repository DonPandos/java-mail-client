package com.study.course4.emailclient;

import com.study.course4.emailclient.controller.MainFormController;
import com.study.course4.emailclient.controller.StartMenuController;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.AccountService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import static com.study.course4.emailclient.controller.MainFormController.currentAccount;
import static com.study.course4.emailclient.controller.MainFormController.mailSession;
import static com.study.course4.emailclient.controller.MainFormController.mailSessions;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(SpringBootMain.class)
                .run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    @SneakyThrows
    public void start(Stage stage) {
        List<String> accounts = Files.readAllLines(new File("src/main/java/com/study/course4/emailclient/files/accounts.txt").toPath());
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        mailSessions = new HashMap<>();

        if(!accounts.isEmpty()) {
            String account = accounts.get(0);
            String email = account.substring(0, account.indexOf(" "));
            String password = account.substring(account.indexOf(" ") + 1, account.length());
            mailSession = applicationContext.getBean(MailSession.class, email, password);

            for(String acc : accounts) {
                mailSessions.put(acc, acc.equals(email + " " + password) ? mailSession : null);
            }
            currentAccount = email + " " + password;

            Scene scene = new Scene(fxWeaver.loadView(MainFormController.class));

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } else {
            Parent root = fxWeaver.loadView(StartMenuController.class);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
    }
}
