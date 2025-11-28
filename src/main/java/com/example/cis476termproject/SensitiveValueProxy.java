package com.example.cis476termproject;

import java.util.Objects;

/**
 * Proxy wrapper for sensitive values to control how they are displayed and copied.
 */
public class SensitiveValueProxy {
    private String value;
    private final boolean masked;

    public SensitiveValueProxy(String value, boolean masked) {
        this.value = Objects.requireNonNullElse(value, "");
        this.masked = masked;
    }

    public String getDisplayValue() {
        return masked ? buildMask() : value;
    }

    public String getValueForCopy() {
        return value;
    }

    public void updateValue(String newValue) {
        this.value = Objects.requireNonNullElse(newValue, "");
    }

    private String buildMask() {
        if (value.isEmpty()) {
            return "";
        }
        int maskLength = Math.min(Math.max(value.length(), 4), 12);
        return "\u2022".repeat(maskLength);
    }
}
