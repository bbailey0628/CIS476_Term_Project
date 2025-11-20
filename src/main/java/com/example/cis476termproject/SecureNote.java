package com.example.cis476termproject;

public class SecureNote {
    private int ID;
    private int OwnerID;
    private String Contents;

    public SecureNote(int id, int ownerID, String contents) {
        setID(id);
        setOwnerID(ownerID);
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

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }
}
