package com.example.cis476termproject;

import javafx.util.Duration;

import static com.example.cis476termproject.ClipboardManager.clearClipboard;

public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private static final Duration INACTIVITY_TIMEOUT = Duration.minutes(2);
    private UserLogin loggedInUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public synchronized void setLoggedInUser(UserLogin userLogin) {
        this.loggedInUser = userLogin;
        refreshActivity();
    }

    public synchronized UserLogin getLoggedInUser() {
        return loggedInUser;
    }

    public synchronized void refreshActivity() {
    }

    public Duration getInactivityTimeout() {
        return INACTIVITY_TIMEOUT;
    }

    public synchronized void clearSession() {
        clearSensitiveData();
    }

    public synchronized void clearSensitiveData() {
        if (loggedInUser != null) {
            loggedInUser.setPassword(null);
            loggedInUser.setSecurityAnswer1(null);
            loggedInUser.setSecurityAnswer2(null);
            loggedInUser.setSecurityAnswer3(null);
            loggedInUser = null;
        }
        clearClipboard();
    }
}
