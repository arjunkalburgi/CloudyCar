package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-10-13.
 */

public interface IUserService {
    User getUser(String username);
    void add(User user);
}
