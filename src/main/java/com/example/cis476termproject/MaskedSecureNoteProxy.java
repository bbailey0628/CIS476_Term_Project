package com.example.cis476termproject;

public class MaskedSecureNoteProxy {
    private final SecureNote secureNote;
    private boolean revealed;

    public MaskedSecureNoteProxy(SecureNote secureNote) {
        this.secureNote = secureNote;
        this.revealed = false;
    }

    public SecureNote getSecureNote() {
        return secureNote;
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

    public String getTitle() {
        return secureNote.getTitle();
    }

    public String getNote() {
        return mask(secureNote.getContents());
    }

    public String getUnmaskedNote() {
        return secureNote.getContents();
    }

    private String mask(String value) {
        if (revealed) {
            return value;
        }
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.length() <= 4) {
            return "*".repeat(value.length());
        }
        return value.charAt(0) + "*".repeat(Math.max(1, value.length() - 2)) + value.charAt(value.length() - 1);
    }
}
