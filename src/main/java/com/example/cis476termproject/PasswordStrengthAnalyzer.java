package com.example.cis476termproject;

public class PasswordStrengthAnalyzer {

    public PasswordStrength analyze(String password) {
        if (password == null || password.isBlank()) {
            return PasswordStrength.WEAK;
        }

        int score = 0;
        if (password.length() >= 12) {
            score += 2;
        } else if (password.length() >= 8) {
            score += 1;
        } else {
            score -= 1;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        if (hasUpper) {
            score++;
        }
        if (hasLower) {
            score++;
        }
        if (hasDigit) {
            score++;
        }
        if (hasSymbol) {
            score++;
        }

        if (score >= 5) {
            return PasswordStrength.STRONG;
        }
        if (score >= 3) {
            return PasswordStrength.MEDIUM;
        }
        return PasswordStrength.WEAK;
    }
}
