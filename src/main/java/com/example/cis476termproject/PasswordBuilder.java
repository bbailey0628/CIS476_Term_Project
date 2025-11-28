package com.example.cis476termproject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * PasswordBuilder uses the Builder pattern to assemble passwords with
 * configurable length and complexity flags.
 */
public class PasswordBuilder {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final int length;
    private final boolean includeUppercase;
    private final boolean includeLowercase;
    private final boolean includeDigits;
    private final boolean includeSymbols;

    private PasswordBuilder(Builder builder) {
        this.length = builder.length;
        this.includeUppercase = builder.includeUppercase;
        this.includeLowercase = builder.includeLowercase;
        this.includeDigits = builder.includeDigits;
        this.includeSymbols = builder.includeSymbols;
    }

    public String buildPassword() {
        StringBuilder pool = new StringBuilder();
        List<Character> required = new ArrayList<>();

        if (includeUppercase) {
            pool.append(UPPERCASE);
            required.add(randomChar(UPPERCASE));
        }
        if (includeLowercase) {
            pool.append(LOWERCASE);
            required.add(randomChar(LOWERCASE));
        }
        if (includeDigits) {
            pool.append(DIGITS);
            required.add(randomChar(DIGITS));
        }
        if (includeSymbols) {
            pool.append(SYMBOLS);
            required.add(randomChar(SYMBOLS));
        }

        if (pool.length() == 0) {
            throw new IllegalStateException("At least one character set must be enabled to build a password.");
        }

        int targetLength = Math.max(length, required.size());
        StringBuilder password = new StringBuilder(targetLength);

        // ensure required characters are placed
        for (char ch : required) {
            password.append(ch);
        }

        for (int i = password.length(); i < targetLength; i++) {
            password.append(randomChar(pool.toString()));
        }

        // shuffle to avoid predictable placement of required characters
        return shuffle(password);
    }

    private char randomChar(String alphabet) {
        return alphabet.charAt(RANDOM.nextInt(alphabet.length()));
    }

    private String shuffle(StringBuilder input) {
        for (int i = input.length() - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = input.charAt(i);
            input.setCharAt(i, input.charAt(j));
            input.setCharAt(j, tmp);
        }
        return input.toString();
    }

    public static class Builder {
        private int length = 16;
        private boolean includeUppercase = true;
        private boolean includeLowercase = true;
        private boolean includeDigits = true;
        private boolean includeSymbols = true;

        public Builder length(int length) {
            this.length = Math.max(1, length);
            return this;
        }

        public Builder includeUppercase(boolean includeUppercase) {
            this.includeUppercase = includeUppercase;
            return this;
        }

        public Builder includeLowercase(boolean includeLowercase) {
            this.includeLowercase = includeLowercase;
            return this;
        }

        public Builder includeDigits(boolean includeDigits) {
            this.includeDigits = includeDigits;
            return this;
        }

        public Builder includeSymbols(boolean includeSymbols) {
            this.includeSymbols = includeSymbols;
            return this;
        }

        public PasswordBuilder build() {
            return new PasswordBuilder(this);
        }
    }
}
