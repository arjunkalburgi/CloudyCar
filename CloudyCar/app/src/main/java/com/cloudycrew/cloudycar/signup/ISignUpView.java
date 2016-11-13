package com.cloudycrew.cloudycar.signup;

/**
 * Created by George on 2016-11-09.
 */
public interface ISignUpView {
    /**
     * Callback when user registration fails due to a malformed user
     */
    void onMalformedUserFailure();

    /**
     * Callback when user registration fails due to a duplicate user
     */
    void onDuplicateUsernameFailure();

    /**
     * Callback for when user registration is successful
     */
    void onSuccessfulRegistration();
}
