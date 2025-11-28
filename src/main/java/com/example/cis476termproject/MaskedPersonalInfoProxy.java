package com.example.cis476termproject;

public class MaskedPersonalInfoProxy {
    private final PersonalInfo personalInfo;
    private boolean revealed;

    public MaskedPersonalInfoProxy(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
        this.revealed = false;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
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

    public String getLicenseNumber() {
        return mask(personalInfo.getLicenseNumber());
    }

    public String getSocialSecurityNumber() {
        return maskNumber(String.valueOf(personalInfo.getSocialSecurityNumber()));
    }

    public String getPassportNumber() {
        return mask(personalInfo.getPassportNumber());
    }

    public String getUnmaskedLicenseNumber() {
        return personalInfo.getLicenseNumber();
    }

    public String getUnmaskedSocialSecurityNumber() {
        return String.valueOf(personalInfo.getSocialSecurityNumber());
    }

    public String getUnmaskedPassportNumber() {
        return personalInfo.getPassportNumber();
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

    private String maskNumber(String value) {
        if (revealed) {
            return value;
        }
        int length = value.length();
        if (length <= 4) {
            return "*".repeat(length);
        }
        String lastFour = value.substring(length - 4);
        return "*".repeat(length - 4) + lastFour;
    }
}
