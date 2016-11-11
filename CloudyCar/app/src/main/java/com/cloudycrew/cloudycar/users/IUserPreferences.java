package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-11-10.
 */

public interface IUserPreferences {
    String getUserName();
    String getEmail();
    String getPhoneNumber();
    void saveUser(User user);
}
