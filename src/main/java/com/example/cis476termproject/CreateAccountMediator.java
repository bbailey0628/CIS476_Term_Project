package com.example.cis476termproject;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateAccountMediator implements FormMediator {

    private TextField passwordField;
    private Label passwordWarningLabel;

    @Override
    public void registerPasswordField(TextField passwordField) {
        this.passwordField = passwordField;
    }

    @Override
    public void registerWarningLabel(Label warningLabel) {
        this.passwordWarningLabel = warningLabel;
    }

    @Override
    public void applyGeneratedPassword(String password) {
        if (passwordField != null) {
            passwordField.setText(password);
        }
    }

    @Override
    public void showWeakPasswordWarning(String message) {
        if (passwordWarningLabel != null) {
            passwordWarningLabel.setText(message);
            passwordWarningLabel.setVisible(true);
        }
    }

    @Override
    public void clearWeakPasswordWarning() {
        if (passwordWarningLabel != null) {
            passwordWarningLabel.setText("");
            passwordWarningLabel.setVisible(false);
        }
    }
}
