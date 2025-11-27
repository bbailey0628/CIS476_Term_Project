package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateAccountController {

    @FXML
    private Button createAccountButton;
    @FXML
    private TextField securityAnswer1;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField securityQuestion3;
    @FXML
    private TextField securityQuestion2;
    @FXML
    private TextField securityQuestion1;
    @FXML
    private TextField securityAnswer3;
    @FXML
    private TextField securityAnswer2;
    @FXML
    private Button backButton;

    @FXML
    public void CreateAccountButtonClicked() {
        /* Make sure to check if the email is in use already before creating a new account
        emailField.getText();
        passwordField.getText();
        securityQuestion1.getText();
        securityAnswer1.getText();
        securityQuestion2.getText();
        securityAnswer2.getText();
        securityQuestion3.getText();
        securityAnswer3.getText();
        We have to use the mediator pattern to manage communication between UI components, so can't pull email and password info directly from text fields
         */
    }

    @FXML
    public void BackButtonClicked() {
        try {
            MyPassApplication.switchScene("login-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
