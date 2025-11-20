package com.example.cis476termproject;

public class CreditCard {
    private int ID;
    private int OwnerID;
    private String Issuer;
    private int CreditCardNumber;
    private String CardholderName;
    private int ExpirationDate;
    private int CCVCode;

    public CreditCard(int id, int ownerID, String issuer, int creditCardNumber, String cardholderName, int expirationDate, int ccvCode) {
        setID(id);
        setOwnerID(ownerID);
        setIssuer(issuer);
        setCreditCardNumber(creditCardNumber);
        setCardholderName(cardholderName);
        setExpirationDate(expirationDate);
        setCCVCode(ccvCode);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(int ownerID) {
        OwnerID = ownerID;
    }

    public String getIssuer() {
        return Issuer;
    }

    public void setIssuer(String issuer) {
        Issuer = issuer;
    }

    public int getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(int creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String getCardholderName() {
        return CardholderName;
    }

    public void setCardholderName(String cardholderName) {
        CardholderName = cardholderName;
    }

    public int getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        ExpirationDate = expirationDate;
    }

    public int getCCVCode() {
        return CCVCode;
    }

    public void setCCVCode(int CCVCode) {
        this.CCVCode = CCVCode;
    }
}
