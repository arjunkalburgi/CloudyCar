package com.cloudycrew.cloudycar.signup;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.email.Email;
import com.cloudycrew.cloudycar.models.PhoneNumber;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

/**
 * Created by George on 2016-11-09.
 */

public class SignUpController extends ViewController<ISignUpView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    public SignUpController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    public void registerUser(String username, String email, String phoneNumber) {
        try {
            User newUser = createUser(username, email, phoneNumber);
            registerUser(newUser);
        } catch (Exception e) {
            dispatchOnMalformedUserFailure();
        }
    }

    private void registerUser(User user) {
        ObservableUtils.fromAction(userController::createUser, user)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(nothing -> dispatchOnSuccessfulRegistration(),
                                  throwable -> dispatchOnDuplicateUsernameFailure());

    }

    private User createUser(String username, String email, String phoneNumber) {
        User newUser = new User(username);
        newUser.setEmail(new Email(email));
        newUser.setPhoneNumber(new PhoneNumber(email));

        return newUser;
    }


    private void dispatchOnMalformedUserFailure() {
        if (isViewAttached()) {
            getView().onMalformedUserFailure();
        }
    }

    private void dispatchOnDuplicateUsernameFailure() {
        if (isViewAttached()) {
            getView().onDuplicateUsernameFailure();
        }
    }

    private void dispatchOnSuccessfulRegistration() {
        if (isViewAttached()) {
            getView().onSuccessfulRegistration();
        }
    }
}
