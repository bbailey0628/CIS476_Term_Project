package com.example.cis476termproject;

public class PersonalInfo {
    private int ID;
    private int OwnerID;
    private String LicenseNumber;
    private String SocialSecurityNumber;
    private String PassportNumber;

    // First constructor
    public PersonalInfo(int id, int ownerID, String licenseNumber, String socialSecurityNumber, String passportNumber) {
        setID(id);
        setOwnerID(ownerID);
        setLicenseNumber(licenseNumber);
        setSocialSecurityNumber(socialSecurityNumber);
        setPassportNumber(passportNumber);
    }

    // Second constructor
    public PersonalInfo(int ownerID, String licenseNumber, String socialSecurityNumber, String passportNumber) {
        setOwnerID(ownerID);
        setLicenseNumber(licenseNumber);
        setSocialSecurityNumber(socialSecurityNumber);
        setPassportNumber(passportNumber);
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

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        LicenseNumber = licenseNumber;
    }

    public String getSocialSecurityNumber() {
        return SocialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        SocialSecurityNumber = socialSecurityNumber;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        PassportNumber = passportNumber;
    }
}