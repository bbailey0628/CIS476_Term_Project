package com.example.cis476termproject;

public class MaskedCreditCardProxy {
    private final CreditCard creditCard;
    private boolean revealed;

    public MaskedCreditCardProxy(CreditCard creditCard) {
        this.creditCard = creditCard;
        this.revealed = false;
    }

    public CreditCard getCreditCard() {
        return creditCard;
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

    public String getIssuer() {
        return creditCard.getIssuer();
    }

    public String getCreditCardNumber() {
        return maskNumber(String.valueOf(creditCard.getCreditCardNumber()));
    }

    public String getCardholderName() {
        return mask(creditCard.getCardholderName());
    }

    public String getExpirationDate() {
        return maskNumber(String.valueOf(creditCard.getExpirationDate()));
    }

    public String getCcvCode() {
        return maskNumber(String.valueOf(creditCard.getCCVCode()));
    }

    public String getUnmaskedCreditCardNumber() {
        return String.valueOf(creditCard.getCreditCardNumber());
    }

    public String getUnmaskedCardholderName() {
        return creditCard.getCardholderName();
    }

    public String getUnmaskedExpirationDate() {
        return String.valueOf(creditCard.getExpirationDate());
    }

    public String getUnmaskedCcvCode() {
        return String.valueOf(creditCard.getCCVCode());
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
