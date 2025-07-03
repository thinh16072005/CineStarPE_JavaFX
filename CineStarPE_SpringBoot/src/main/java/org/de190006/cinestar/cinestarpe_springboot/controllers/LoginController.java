package org.de190006.cinestar.cinestarpe_springboot.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.de190006.cinestar.cinestarpe_springboot.repositories.AccountRepository;
import org.de190006.cinestar.cinestarpe_springboot.models.Account;
import org.de190006.cinestar.cinestarpe_springboot.controllers.MovieController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class LoginController {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label errorLabel;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Value("classpath:/templates/movies.fxml")
    private Resource movieResources;

    @FXML
    protected void onLoginButtonClick() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (accountRepository == null) {
            errorLabel.setText("Repository not set!");
            errorLabel.setStyle("-fx-text-fill: #e53935;");
            return;
        }

        Optional<Account> acc = accountRepository.findByEmailAndPassword(email, password);
        if (acc.isPresent()) {
            int role = acc.get().getRoleID();
            if (role == 1 || role == 3) {
                errorLabel.setText("Login successful!");
                errorLabel.setStyle("-fx-text-fill: green;");
                
                // Redirect to movie page
                try {
                    FXMLLoader loader = new FXMLLoader(movieResources.getURL());
                    // Set the controller factory to use Spring beans
                    loader.setControllerFactory(applicationContext::getBean);
                    
                    Parent root = loader.load();
                    
                    // Get the MovieController and set the user
                    MovieController movieController = loader.getController();
                    movieController.setUser(acc.get());
                    
                    Stage stage = (Stage) txtEmail.getScene().getWindow();
                    stage.setTitle("Movies Management - CineStar");
                    stage.setScene(new Scene(root, 1000, 700));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    errorLabel.setText("Failed to load movie page: " + e.getMessage());
                    errorLabel.setStyle("-fx-text-fill: #e53935;");
                }

            } else {
                errorLabel.setText("You have no permission to access this function!");
                errorLabel.setStyle("-fx-text-fill: #e53935;");
            }
        } else {
            errorLabel.setText("Invalid credentials!");
            errorLabel.setStyle("-fx-text-fill: #e53935;");
        }
    }
}