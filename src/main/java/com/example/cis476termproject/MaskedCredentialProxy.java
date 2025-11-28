package com.example.cis476termproject;

public class MaskedCredentialProxy {
    private final Credentials credentials;
    private boolean revealed;

    public MaskedCredentialProxy(Credentials credentials) {
        this.credentials = credentials;
        this.revealed = false;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void reveal() {
        this.revealed = true;
    }

    public void hide() {
        this.revealed = false;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public String getURL() {
        return credentials.getURL();
    }

    public String getUsername() {
        return mask(credentials.getUsername());
    }

    public String getPassword() {
        return mask(credentials.getPassword());
    }

    public String getUnmaskedUsername() {
        return credentials.getUsername();
    }

    public String getUnmaskedPassword() {
        return credentials.getPassword();
    }

    private String mask(String value) {
        if (revealed) {
            return value;
        }
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.length() <= 2) {
            return "*".repeat(value.length());
        }
        return value.charAt(0) + "*".repeat(Math.max(1, value.length() - 2)) + value.charAt(value.length() - 1);
    }
}
