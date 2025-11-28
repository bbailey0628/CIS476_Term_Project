package com.example.cis476termproject;

public interface PasswordObservable {
    void addObserver(PasswordObserver observer);

    void removeObserver(PasswordObserver observer);

    void updatePassword(String password);
}
