package com.example.cis476termproject;

public class SecurityQuestionHandler {
    private final String question;
    private final String expectedAnswer;
    private SecurityQuestionHandler nextHandler;

    public SecurityQuestionHandler(String question, String expectedAnswer) {
        this.question = question;
        this.expectedAnswer = expectedAnswer;
    }

    public void setNextHandler(SecurityQuestionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public SecurityQuestionHandler getNextHandler() {
        return nextHandler;
    }

    public String getQuestion() {
        return question;
    }

    public boolean handle(String providedAnswer) {
        if (providedAnswer == null || expectedAnswer == null) {
            return false;
        }
        return expectedAnswer.equalsIgnoreCase(providedAnswer.trim());
    }
}
