package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;

import rx.Observable;

/**
 * Created by George on 2016-11-05.
 */

public class UserProfileController extends ViewController<IUserProfileView> {
    private UserController userController;

    public UserProfileController(UserController userController) {
        this.userController = userController;
    }

    public void loadUser(String username) {

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
}
