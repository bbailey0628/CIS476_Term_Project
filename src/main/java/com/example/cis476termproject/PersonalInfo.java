package com.example.cis476termproject;

public class PersonalInfo {
    private int OwnerID;
    private String LicenseNumber;
    private int SocialSecurityNumber;
    private String PassportNumber;

    public PersonalInfo(int ownerID, String licenseNumber, int socialSecurityNumber, String passportNumber) {
        setOwnerID(ownerID);
        setLicenseNumber(licenseNumber);
        setSocialSecurityNumber(socialSecurityNumber);
        setPassportNumber(passportNumber);
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

    public int getSocialSecurityNumber() {
        return SocialSecurityNumber;
    }

    public void setSocialSecurityNumber(int socialSecurityNumber) {
        SocialSecurityNumber = socialSecurityNumber;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        PassportNumber = passportNumber;
    }
}
