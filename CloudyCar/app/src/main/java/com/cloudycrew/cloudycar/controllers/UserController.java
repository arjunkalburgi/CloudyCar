package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.users.DuplicateUserException;
import com.cloudycrew.cloudycar.users.IUserService;
import com.cloudycrew.cloudycar.users.IncompleteUserException;

/**
 * Created by George on 2016-10-23.
 */

public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
    public void createUser(User user) throws DuplicateUserException, IncompleteUserException {
        userService.createUser(user);
    }

    public User getUser(String username) {
        return userService.getUser(username);
    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    public void updateUser(User user) {
        userService.updateUser(user);
    }
}
