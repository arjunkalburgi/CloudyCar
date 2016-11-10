package com.cloudycrew.cloudycar.controllers;

import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.users.IUserService;

/**
 * Created by George on 2016-10-23.
 */

public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {

    }
    public void createUser() {

    }

    public User getUser(String username) {
        return null;
    }
}
