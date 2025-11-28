package com.example.cis476termproject;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthSubject {
    private final List<PasswordObserver> observers = new ArrayList<>();

    /**
     * Adds an observer if it is not already present.
     *
     * @param observer The observer to add.
     */
    public void addObserver(PasswordObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer.
     *
     * @param observer The observer to remove.
     */
    public void removeObserver(PasswordObserver observer) {
        observers.remove(observer);
    }

    /**
     * Updates the password, evaluates its strength, and notifies all observers.
     *
     * @param password The password to evaluate.
     * @return The evaluated password strength.
     */
    public PasswordStrength updatePassword(String password) {
        PasswordStrength strength = evaluate(password);
        notifyObservers(strength, password);
        return strength;
    }

    /**
     * Evaluates the password strength based on specific criteria.
     *
     * @param password The password to evaluate.
     * @return The evaluated password strength.
     */
    private PasswordStrength evaluate(String password) {
        if (password == null || password.isBlank()) {
            return PasswordStrength.WEAK;
        }

        int score = 0;

        // Check for minimum length
        if (password.length() >= 8) {
            score++;
        }
        // Check for at least one uppercase letter
        if (password.matches(".*[A-Z].*")) {
            score++;
        }
        // Check for at least one lowercase letter
        if (password.matches(".*[a-z].*")) {
            score++;
        }
        // Check for at least one digit
        if (password.matches(".*\\d.*")) {
            score++;
        }
        // Check for at least one special character
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            score++;
        }

        // Determine password strength based on score
        if (score >= 4) {
            return PasswordStrength.STRONG;
        }
        if (score >= 2) {
            return PasswordStrength.MEDIUM;
        }
        return PasswordStrength.WEAK;
    }

    /**
     * Notifies all registered observers with the latest password strength and password.
     *
     * @param strength The password strength.
     * @param password The password being evaluated.
     */
    private void notifyObservers(PasswordStrength strength, String password) {
        for (PasswordObserver observer : observers) {
            observer.onPasswordStrengthChanged(strength, password);
        }
    }
}