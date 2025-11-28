package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HomeController {

    @FXML
    private Button SecureNoteButton;
    @FXML
    private Button CreditCardButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button CredentialsButton;
    @FXML
    private Button PersonalInfoButton;
    @FXML
    private Label WelcomeText;

    @FXML
    public void CreditCardButtonClicked() {
        try {
            MyPassApplication.switchScene("creditCard-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void CredentialsButtonClicked() {
        try {
            MyPassApplication.switchScene("credentials-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void SecureNoteButtonClicked() {
        try {
            MyPassApplication.switchScene("secureNote-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void PersonalInfoButtonClicked() {
        try {
            MyPassApplication.switchScene("personalInfo-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void LogoutButtonClicked() {
        try {
            SessionManager.getInstance().clearSession();
            MyPassApplication.switchScene("login-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
