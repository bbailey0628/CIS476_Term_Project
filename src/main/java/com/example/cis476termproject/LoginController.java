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
    private TextField passwordField;

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
        /*
        Create login function that grabs the user based on the email provided
        Then check to ensure the password matches the one from the user
        We have to use the mediator pattern to manage communication between UI components, so can't pull email and password info directly from text fields
         */
        try {
            MyPassApplication.switchScene("home-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
