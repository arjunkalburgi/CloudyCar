package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by George on 2016-11-05.
 */

public class UserProfileController extends ViewController<IUserProfileView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    public UserProfileController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Asynchronously loads a user
     * @param username - username of the user to load
     */
    public void loadUser(final String username) {
        dispatchDisplayLoading();
        User localUser = userController.getCurrentUser();
        if( !username.equals(localUser.getUsername()) ) {
            ObservableUtils.create(new Func0<User>() {
                                @Override
                                public User call() {
                                    return userController.getUser(username);
                                }
                            })
                            .subscribeOn(schedulerProvider.ioScheduler())
                            .observeOn(schedulerProvider.mainThreadScheduler())
                            .subscribe(new Action1<User>() {
                                @Override
                                public void call(User user) {
                                    dispatchDisplayUser(user);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    dispatchUserDoesNotExist();
                                }
                            });
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

    private void dispatchUserDoesNotExist() {
        if (getView() != null) {
            getView().displayErrorToast();
        }
    }
}
