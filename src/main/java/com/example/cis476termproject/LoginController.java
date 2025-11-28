package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private Button createAccountButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button forgotPasswordButton;
    @FXML
    private Label loginFeedbackLabel;

    @FXML
    public void CreateAccountButtonClicked() {
        try {
            MyPassApplication.switchScene("createAccount-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void LoginButtonClicked() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            setFeedback("Please enter both email and password.");
            return;
        }

        UserLogin existingUser = VaultDB.getUserLoginByEmail(email.trim());
        if (existingUser == null) {
            setFeedback("Invalid email or password.");
            return;
        }

        String hashedInput = PasswordHasher.hashPassword(password);
        if (!hashedInput.equals(existingUser.getPassword())) {
            setFeedback("Invalid email or password.");
            return;
        }

        try {
            SessionManager.getInstance().setLoggedInUser(existingUser);
            setFeedback("");
            MyPassApplication.switchScene("home-view.fxml");
        } catch (Exception e) {
            setFeedback("Unable to complete login: " + e.getMessage());
        }
    }

    @FXML
    public void ForgotPasswordClicked() {
        try {
            MyPassApplication.switchScene("recovery-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFeedback(String message) {
        if (loginFeedbackLabel != null) {
            loginFeedbackLabel.setText(message);
        }
    }
}
