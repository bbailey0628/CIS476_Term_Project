package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    private final AuthFormMediator authFormMediator = new AuthFormMediator();

    @FXML
    private Button createAccountButton;
    @FXML
    private TextField securityAnswer1;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label passwordWarningLabel;
    @FXML
    private Button generatePasswordButton;
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
    private Label validationMessage;

    private final PasswordBuilder passwordBuilder = new PasswordBuilder.Builder().build();
    private final FormMediator mediator = new CreateAccountMediator();
    private final PasswordStrengthObservable passwordObservable = new PasswordStrengthObservable(new PasswordStrengthAnalyzer());

    @FXML
    public void CreateAccountButtonClicked() {
        validationMessage.setText("");
        AuthFormMediator.ValidationResult result = authFormMediator.handleCreateAccount(this);

        if (result.isSuccess()) {
            try {
                MyPassApplication.switchScene("login-view.fxml");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            validationMessage.setText(result.getMessage());
        }
    }

    @FXML
    public void BackButtonClicked() {
        try {
            MyPassApplication.switchScene("login-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void GeneratePasswordButtonClicked() {
        String generatedPassword = passwordBuilder.buildPassword();
        mediator.applyGeneratedPassword(generatedPassword);
        passwordObservable.updatePassword(generatedPassword);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediator.registerPasswordField(passwordField);
        mediator.registerWarningLabel(passwordWarningLabel);
        passwordWarningLabel.setVisible(false);

        passwordObservable.addObserver(new WeakPasswordObserver(mediator));

        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                passwordObservable.updatePassword(newValue)
        ); // Close the method here properly
    }

    public String getEmailInput() {
        return emailField.getText();
    }

    public String getPasswordInput() {
        return passwordField.getText();
    }

    public String getSecurityQuestion1() {
        return securityQuestion1.getText();
    }

    public String getSecurityAnswer1() {
        return securityAnswer1.getText();
    }

    public String getSecurityQuestion2() {
        return securityQuestion2.getText();
    }

    public String getSecurityAnswer2() {
        return securityAnswer2.getText();
    }

    public String getSecurityQuestion3() {
        return securityQuestion3.getText();
    }

    public String getSecurityAnswer3() {
        return securityAnswer3.getText();
    }}
