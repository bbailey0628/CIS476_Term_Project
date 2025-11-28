package com.example.cis476termproject;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthObservable implements PasswordObservable {

    private final List<PasswordObserver> observers = new ArrayList<>();
    private final PasswordStrengthAnalyzer analyzer;

    public PasswordStrengthObservable(PasswordStrengthAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public void addObserver(PasswordObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PasswordObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void updatePassword(String password) {
        PasswordStrength strength = analyzer.analyze(password);
        notifyObservers(password, strength);
    }

    private void notifyObservers(String password, PasswordStrength strength) {
        for (PasswordObserver observer : observers) {
            observer.onPasswordChanged(password, strength);
        }
    }
}
