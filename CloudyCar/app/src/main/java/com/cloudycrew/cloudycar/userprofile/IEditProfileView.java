package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by Ryan on 2016-11-12.
 */

public interface IEditProfileView {
    /**
     * Callback for when loading starts
     */
    void displayLoading();

    /**
     * Callback for displaying a user
     * @param user - the user to be displaued
     */
    void displayUser(User user);

    /**
     * Callback for when editing a user succeeds
     */
    void onEditSuccess();

    /**
     * Callback for when editing a user fails
     */
    void onEditFailure();
}
