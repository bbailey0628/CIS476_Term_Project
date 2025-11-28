package com.example.cis476termproject;

public interface PasswordObserver {
    void onPasswordStrengthChanged(PasswordStrength strength, String password);
    void onPasswordChanged(String password, PasswordStrength strength);
}
