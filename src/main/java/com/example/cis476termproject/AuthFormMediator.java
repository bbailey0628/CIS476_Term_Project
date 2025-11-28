package com.example.cis476termproject;

public class AuthFormMediator {

    public ValidationResult handleCreateAccount(CreateAccountController controller) {
        String email = controller.getEmailInput();
        String password = controller.getPasswordInput();
        String securityQuestion1 = controller.getSecurityQuestion1();
        String securityAnswer1 = controller.getSecurityAnswer1();
        String securityQuestion2 = controller.getSecurityQuestion2();
        String securityAnswer2 = controller.getSecurityAnswer2();
        String securityQuestion3 = controller.getSecurityQuestion3();
        String securityAnswer3 = controller.getSecurityAnswer3();

        if (isAnyBlank(email, password, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2, securityQuestion3, securityAnswer3)) {
            return ValidationResult.failure("Please complete all fields before continuing.");
        }

        UserLogin existingUser = VaultDB.getUserLoginByEmail(email);
        if (existingUser != null) {
            return ValidationResult.failure("An account with that email already exists.");
        }

        String hashedPassword = PasswordHasher.hashPassword(password);
        UserLogin userLogin = new UserLogin(email, hashedPassword, securityQuestion1, securityAnswer1, securityQuestion2, securityAnswer2, securityQuestion3, securityAnswer3);
        VaultDB.addUserLogin(userLogin);
        return ValidationResult.success();
    }

    private boolean isAnyBlank(String... values) {
        for (String value : values) {
            if (value == null || value.isBlank()) {
                return true;
            }
        }
        return false;
    }

    public static class ValidationResult {
        private final boolean success;
        private final String message;

        private ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, "");
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
