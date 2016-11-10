package com.cloudycrew.cloudycar.signup;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
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

    public void registerUser(User user) {
        ObservableUtils.fromAction(userController::createUser, user)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(nothing -> dispatchOnSuccessfulRegistration(),
                                  throwable -> dispatchOnDuplicateUsernameFailure());
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
