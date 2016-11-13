package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

/**
 * Created by Ryan on 2016-11-12.
 */

public class EditProfileController extends ViewController<IEditProfileView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    public EditProfileController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Asynchronously loads a user
     * @param username - username of the user to load
     */
    public void loadUser(String username) {
        dispatchDisplayLoading();
        User localUser = userController.getCurrentUser();
        if( !username.equals(localUser.getUsername()) ) {
            ObservableUtils.fromFunction(userController::getUser, username)
                    .subscribeOn(schedulerProvider.ioScheduler())
                    .observeOn(schedulerProvider.mainThreadScheduler())
                    .subscribe(this::dispatchDisplayUser);
        }
        else {
            this.dispatchDisplayUser(localUser);
        }

    }

    private void dispatchDisplayLoading() {
        if (getView() != null) {
            getView().displayLoading();
        }
    }

    private void dispatchDisplayUser(User user) {
        if (getView() != null) {
            getView().displayUser(user);
        }
    }

    /**
     * Asynchronously updates a user locally and remotely, calls appropriate callbacks for failures
     * @param user - the user to update
     */
    public void updateUser(User user) {
        ObservableUtils.fromAction(userController::updateCurrentUser, user)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(nothing -> dispatchOnEditSuccess(),
                                  throwable -> dispatchOnEditFailure());
    }

    private void dispatchOnEditSuccess() {
        if (isViewAttached()) {
            getView().onEditSuccess();
        }
    }

    private void dispatchOnEditFailure() {
        if (isViewAttached()) {
            getView().onEditFailure();
        }
    }
}
