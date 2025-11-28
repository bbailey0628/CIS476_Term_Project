package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecoveryController implements PasswordObserver {

    @FXML
    private TextField emailField;
    @FXML
    private Button loadUserButton;
    @FXML
    private Label questionLabel;
    @FXML
    private TextField answerField;
    @FXML
    private Button submitAnswerButton;
    @FXML
    private Label feedbackLabel;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button resetPasswordButton;
    @FXML
    private Label strengthLabel;
    @FXML
    private Label passwordStatusLabel;

    private UserLogin recoveredUser;
    private SecurityQuestionHandler currentHandler;
    private boolean questionsCompleted = false;

    private final PasswordStrengthSubject strengthSubject = new PasswordStrengthSubject();

    @FXML
    public void initialize() {
        strengthSubject.addObserver(this);
        disableSecuritySection(true);
        setPasswordFieldsEnabled(false);
    }

    @FXML
    public void loadUser() {
        String email = emailField.getText();
        if (email == null || email.isBlank()) {
            feedbackLabel.setText("Please enter the email associated with your account.");
            return;
        }

        recoveredUser = VaultDB.getUserLoginByEmail(email.trim());
        if (recoveredUser == null) {
            feedbackLabel.setText("No account found for that email address.");
            resetFlowState();
            return;
        }

        setupSecurityHandlers();
        feedbackLabel.setText("Answer your security questions to continue.");
        questionLabel.setText(currentHandler.getQuestion());
        disableSecuritySection(false);
        setPasswordFieldsEnabled(false);
        answerField.clear();
    }

    @FXML
    public void submitAnswer() {
        if (currentHandler == null) {
            feedbackLabel.setText("Load your account first.");
            return;
        }

        if (!currentHandler.handle(answerField.getText())) {
            feedbackLabel.setText("That answer does not match our records. Please try again.");
            return;
        }

        SecurityQuestionHandler next = currentHandler.getNextHandler();
        if (next != null) {
            currentHandler = next;
            questionLabel.setText(currentHandler.getQuestion());
            feedbackLabel.setText("Correct. Please answer the next question.");
            answerField.clear();
        } else {
            questionsCompleted = true;
            feedbackLabel.setText("All questions verified. You may now reset your password.");
            disableSecuritySection(true);
            setPasswordFieldsEnabled(true);
            handlePasswordInputChanged();
        }
    }

    @FXML
    public void handlePasswordInputChanged() {
        if (!questionsCompleted) {
            resetPasswordButton.setDisable(true);
            return;
        }

        PasswordStrength strength = strengthSubject.updatePassword(newPasswordField.getText());
        boolean passwordsMatch = newPasswordField.getText() != null && newPasswordField.getText().equals(confirmPasswordField.getText());

        if (!passwordsMatch) {
            passwordStatusLabel.setText("Passwords must match.");
        } else {
            passwordStatusLabel.setText("");
        }

        resetPasswordButton.setDisable(!(strength == PasswordStrength.STRONG && passwordsMatch));
    }

    @FXML
    public void resetPassword() {
        if (!questionsCompleted || recoveredUser == null) {
            feedbackLabel.setText("Complete security verification first.");
            return;
        }

        String hashedPassword = PasswordHasher.hashPassword(newPasswordField.getText());
        recoveredUser.setPassword(hashedPassword);
        VaultDB.updateUserLogin(recoveredUser);
        feedbackLabel.setText("Password updated. You can now log in.");

        try {
            MyPassApplication.switchScene("login-view.fxml");
        } catch (Exception e) {
            feedbackLabel.setText("Password updated, but we could not return to login: " + e.getMessage());
        }
    }

    private void setupSecurityHandlers() {
        SecurityQuestionHandler first = new SecurityQuestionHandler(recoveredUser.getSecurityQuestion1(), recoveredUser.getSecurityAnswer1());
        SecurityQuestionHandler second = new SecurityQuestionHandler(recoveredUser.getSecurityQuestion2(), recoveredUser.getSecurityAnswer2());
        SecurityQuestionHandler third = new SecurityQuestionHandler(recoveredUser.getSecurityQuestion3(), recoveredUser.getSecurityAnswer3());

        first.setNextHandler(second);
        second.setNextHandler(third);

        currentHandler = first;
        questionsCompleted = false;
        passwordStatusLabel.setText("");
        answerField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void disableSecuritySection(boolean disable) {
        answerField.setDisable(disable);
        submitAnswerButton.setDisable(disable);
    }

    private void setPasswordFieldsEnabled(boolean enabled) {
        newPasswordField.setDisable(!enabled);
        confirmPasswordField.setDisable(!enabled);
        resetPasswordButton.setDisable(true);
    }

    private void resetFlowState() {
        recoveredUser = null;
        currentHandler = null;
        questionsCompleted = false;
        questionLabel.setText("Security Question");
        answerField.clear();
        disableSecuritySection(true);
        setPasswordFieldsEnabled(false);
        passwordStatusLabel.setText("");
        strengthLabel.setText("Strength: n/a");
    }

    @Override
    public void onPasswordStrengthChanged(PasswordStrength strength, String password) {
        String strengthText = "Strength: " + strength.name();
        strengthLabel.setText(strengthText);
    }

    /**
     * Implements the required `onPasswordChanged` method from `PasswordObserver`.
     *
     * @param password The new password being updated.
     * @param strength The strength of the password.
     */
    @Override
    public void onPasswordChanged(String password, PasswordStrength strength) {
        feedbackLabel.setText("Password changed to a " + strength.name() + " strength.");
    }
}