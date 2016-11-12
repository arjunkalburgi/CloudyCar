package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by Ryan on 2016-11-12.
 */

public interface IEditProfileView {
    void displayLoading();
    void displayUser(User user);
    void onEditSuccess();
    void onEditFailure();
}
