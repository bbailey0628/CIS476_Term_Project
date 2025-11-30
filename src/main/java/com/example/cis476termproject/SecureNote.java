package com.example.cis476termproject;

public class SecureNote {
    private int ID;
    private int OwnerID;
    private String Title;
    private String Contents;

    public SecureNote(int id, int ownerID, String title, String contents) {
        setID(id);
        setOwnerID(ownerID);
        setTitle(title);
        setContents(contents);
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }
}
