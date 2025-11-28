package com.example.cis476termproject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Simple proxy that controls whether a sensitive field is masked or revealed.
 * The display value can be bound directly to a Label or TextField so UI updates
 * as soon as {@link #reveal()} or {@link #hide()} are called.
 */
public class MaskedFieldProxy {
    private final String clearText;
    private final BooleanProperty revealed = new SimpleBooleanProperty(false);
    private final StringProperty displayValue = new SimpleStringProperty();

    public MaskedFieldProxy(String clearText) {
        this.clearText = clearText == null ? "" : clearText;
        updateDisplay();
        revealed.addListener((observable, oldValue, newValue) -> updateDisplay());
    }

    public void reveal() {
        revealed.set(true);
    }

    public void hide() {
        revealed.set(false);
    }

    public boolean isRevealed() {
        return revealed.get();
    }

    public BooleanProperty revealedProperty() {
        return revealed;
    }

    public StringProperty displayValueProperty() {
        return displayValue;
    }

    private void updateDisplay() {
        displayValue.set(revealed.get() ? clearText : mask(clearText));
    }

    private String mask(String value) {
        if (value.isEmpty()) {
            return "";
        }
        return "*".repeat(value.length());
    }
}
