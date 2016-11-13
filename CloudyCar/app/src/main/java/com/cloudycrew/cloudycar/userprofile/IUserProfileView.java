package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-11-05.
 */

public interface IUserProfileView {
    /**
     * Callback for loading
     */
    void displayLoading();

    /**
     * Callback for displaying a user
     * @param user
     */
    void displayUser(User user);

    /**
     * Callback for error in displaying a user, shows a meaningful message in a toast
     */
    void displayErrorToast();
}
