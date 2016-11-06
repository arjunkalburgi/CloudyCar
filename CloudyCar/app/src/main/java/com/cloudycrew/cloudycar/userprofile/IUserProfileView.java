package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-11-05.
 */

public interface IUserProfileView {
    void displayLoading();
    void displayUser(User user);
}
