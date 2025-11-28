package com.example.cis476termproject;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public interface FormMediator {
    void registerPasswordField(TextField passwordField);

    void registerWarningLabel(Label warningLabel);

    void applyGeneratedPassword(String password);

    void showWeakPasswordWarning(String message);

    void clearWeakPasswordWarning();
}
