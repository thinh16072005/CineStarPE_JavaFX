package org.de190006.cinestar.cinestarpe_springboot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<CinestarApp.StageReadyEvent> {

    @Value("classpath:/templates/login.fxml")
    private Resource appResources;

    private String applicationTitle;
    private ApplicationContext context;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle,
                            ApplicationContext context) {
        this.applicationTitle = applicationTitle;
        this.context = context;
    }

    @Override
    public void onApplicationEvent(CinestarApp.StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(appResources.getURL());
            fxmlLoader.setControllerFactory(aClass -> context.getBean(aClass));
            Parent parent = fxmlLoader.load();
            // Set the application title from the properties file
            Stage stage = event.getStage();
            stage.setTitle("Login - CineStar");
            stage.setScene(new Scene(parent, 800, 600));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
