package com.example.cis476termproject;

public class WeakPasswordObserver implements PasswordObserver {

    private final FormMediator mediator;

    /**
     * Constructor for the WeakPasswordObserver.
     *
     * @param mediator The mediator used to interact with the form.
     */
    public WeakPasswordObserver(FormMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Notifies the observer when the password changes.
     *
     * @param password The updated password.
     * @param strength The strength of the updated password.
     */
    @Override
    public void onPasswordChanged(String password, PasswordStrength strength) {
        if (strength == PasswordStrength.WEAK) {
            mediator.showWeakPasswordWarning("Warning: This master password is weak. Consider adding length and variety.");
        } else {
            mediator.clearWeakPasswordWarning();
        }
    }

    /**
     * Notifies the observer when the password strength changes.
     * This method is required by the PasswordObserver interface.
     *
     * @param strength The updated strength of the password.
     * @param password The password being evaluated.
     */
    @Override
    public void onPasswordStrengthChanged(PasswordStrength strength, String password) {
        if (strength == PasswordStrength.WEAK) {
            mediator.showWeakPasswordWarning("Warning: This master password is weak. Consider adding length and variety.");
        } else {
            mediator.clearWeakPasswordWarning();
        }
    }
}