package com.example.cis476termproject;

import javafx.util.Duration;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private static final Duration INACTIVITY_TIMEOUT = Duration.minutes(2);

    private final Set<Object> proxyReferences = new HashSet<>();
    private UserLogin loggedInUser;
    private Instant lastActivity;

    private SessionManager() {
        refreshActivity();
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
        lastActivity = Instant.now();
    }

    public synchronized Instant getLastActivity() {
        return lastActivity;
    }

    public Duration getInactivityTimeout() {
        return INACTIVITY_TIMEOUT;
    }

    public synchronized void registerProxy(Object proxy) {
        if (proxy != null) {
            proxyReferences.add(proxy);
        }
    }

    public synchronized Set<Object> getProxyReferences() {
        return Collections.unmodifiableSet(proxyReferences);
    }

    public synchronized void clearSession() {
        clearSensitiveData();
        lastActivity = null;
    }

    public synchronized void clearSensitiveData() {
        if (loggedInUser != null) {
            loggedInUser.setPassword(null);
            loggedInUser.setSecurityAnswer1(null);
            loggedInUser.setSecurityAnswer2(null);
            loggedInUser.setSecurityAnswer3(null);
            loggedInUser = null;
        }
        proxyReferences.clear();
    }
}
