package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-10-13.
 */

public interface IUserService {

    /**
     * Gets a user
     * @param username - username of the user to be gotten
     * @return the user retrieved from the service
     */
    User getUser(String username);

    /**
     * Creates a user in the service
     * @param user - the user to create
     */
    void createUser(User user);

    /**
     * Gets the currently logged in user from the service
     * @return
     */
    User getCurrentUser();

    /**
     * Sets the currently logged in user
     * @param user - The user to be logged in
     */
    void setCurrentUser(User user);

    /**
     * Updates the user in the service
     * @param user - The user to update
     */
    void updateUser(User user);

    /**
     * Updates the current user in the service
     * @param user - The user to update
     */
    void updateCurrentUser(User user);
}
