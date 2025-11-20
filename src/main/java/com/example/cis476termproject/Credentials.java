package com.example.cis476termproject;

public class Credentials {
    private int ID;
    private int OwnerID;
    private String URL;
    private String Username;
    private String Password;

    public Credentials(int ID, int ownerID, String url, String username, String password) {
        setID(ID);
        setOwnerID(ownerID);
        setURL(url);
        setUsername(username);
        setPassword(password);
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(int ownerID) {
        OwnerID = ownerID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
