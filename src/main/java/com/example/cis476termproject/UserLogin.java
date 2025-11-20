package com.example.cis476termproject;

public class UserLogin {
    private int ID;
    private String Email;
    private String Password;
    private String SecurityQuestion1;
    private String SecurityAnswer1;
    private String SecurityQuestion2;
    private String SecurityAnswer2;
    private String SecurityQuestion3;
    private String SecurityAnswer3;

    public UserLogin(int id, String email, String password, String securityQuestion1, String securityAnswer1, String securityQuestion2, String securityAnswer2, String securityQuestion3, String securityAnswer3) {
        setID(id);
        setEmail(email);
        setPassword(password);
        setSecurityQuestion1(securityQuestion1);
        setSecurityAnswer1(securityAnswer1);
        setSecurityQuestion2(securityQuestion2);
        setSecurityAnswer2(securityAnswer2);
        setSecurityQuestion3(securityQuestion3);
        setSecurityAnswer3(securityAnswer3);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecurityQuestion1() {
        return SecurityQuestion1;
    }

    public void setSecurityQuestion1(String securityQuestion1) {
        SecurityQuestion1 = securityQuestion1;
    }

    public String getSecurityAnswer1() {
        return SecurityAnswer1;
    }

    public void setSecurityAnswer1(String securityAnswer1) {
        SecurityAnswer1 = securityAnswer1;
    }

    public String getSecurityQuestion2() {
        return SecurityQuestion2;
    }

    public void setSecurityQuestion2(String securityQuestion2) {
        SecurityQuestion2 = securityQuestion2;
    }

    public String getSecurityAnswer2() {
        return SecurityAnswer2;
    }

    public void setSecurityAnswer2(String securityAnswer2) {
        SecurityAnswer2 = securityAnswer2;
    }

    public String getSecurityQuestion3() {
        return SecurityQuestion3;
    }

    public void setSecurityQuestion3(String securityQuestion3) {
        SecurityQuestion3 = securityQuestion3;
    }

    public String getSecurityAnswer3() {
        return SecurityAnswer3;
    }

    public void setSecurityAnswer3(String securityAnswer3) {
        SecurityAnswer3 = securityAnswer3;
    }
}
