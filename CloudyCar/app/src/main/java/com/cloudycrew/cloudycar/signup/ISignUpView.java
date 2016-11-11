package com.cloudycrew.cloudycar.signup;

/**
 * Created by George on 2016-11-09.
 */

public interface ISignUpView {
    void onMalformedUserFailure();
    void onDuplicateUsernameFailure();
    void onSuccessfulRegistration();
}
