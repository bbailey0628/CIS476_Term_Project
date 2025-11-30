package com.example.cis476termproject;

public class CreditCard {
    private int ID;
    private int OwnerID;
    private String Issuer;
    private String CreditCardNumber;
    private String CardholderName;
    private String ExpirationDate;
    private String CCVCode;

    public CreditCard(int id, int ownerID, String issuer, String creditCardNumber, String cardholderName, String expirationDate, String ccvCode) {
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

    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String getCardholderName() {
        return CardholderName;
    }

    public void setCardholderName(String cardholderName) {
        CardholderName = cardholderName;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        ExpirationDate = expirationDate;
    }

    public String getCCVCode() {
        return CCVCode;
    }

    public String getCVCCode() {
        return CCVCode;
    }

    public void setCCVCode(String CCVCode) {
        this.CCVCode = CCVCode;
    }

    public void setCVCCode(String CVCCode) {
        this.CCVCode = CVCCode;
    }
}
