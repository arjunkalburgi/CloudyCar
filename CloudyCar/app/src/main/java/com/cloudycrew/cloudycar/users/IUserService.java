package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-10-13.
 */

public interface IUserService {
    User getUser(String username) throws UserDoesNotExistException;
    void createUser(User user) throws DuplicateUserException, IncompleteUserException;
    User getCurrentUser() throws UserDoesNotExistException;
    //Used right now to set the current user at signup but we could also make use of this to log in
    //other users
    void setCurrentUser(User user);
    void updateUser(User user);
}
