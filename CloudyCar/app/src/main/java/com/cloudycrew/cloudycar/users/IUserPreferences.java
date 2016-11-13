package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-11-10.
 */

public interface IUserPreferences {

    /**
     * Gets the username of the local user
     * @return
     */
    String getUserName();

    /**
     * Gets the email address of the local user
     * @return
     */
    String getEmail();

    /**
     * Gets the phone number of the local user
     * @return
     */
    String getPhoneNumber();

    /**
     * Saves the local user
     * @param user
     */
    void saveUser(User user);
}
