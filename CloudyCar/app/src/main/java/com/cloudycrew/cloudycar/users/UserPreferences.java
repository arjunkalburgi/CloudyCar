package com.cloudycrew.cloudycar.users;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.utils.StringUtils;
import com.google.gson.Gson;

/**
 * Created by Ryan on 2016-11-10.
 *
 * A class for storing the information on the currently logged in user - guarantees that certain
 * functionality will be available to the user even when they do not have connectivity.
 *
 */

public class UserPreferences implements IUserPreferences {
    public static final String USERNAME_KEY = "user_name";
    public static final String EMAIL_KEY = "email";
    public static final String PHONENUMBER_KEY = "phone_number";
    public static final String USER_KEY = "user";

    private static final String USER_PREFS = UserPreferences.class.getSimpleName();
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor preferencesEditor;

    public UserPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.preferencesEditor = sharedPrefs.edit();
    }

    @Override
    public String getUserName() {
        return sharedPrefs.getString(USERNAME_KEY, "");
    }

    @Override
    public String getEmail() {
        return sharedPrefs.getString(EMAIL_KEY, "");
    }

    @Override
    public String getPhoneNumber() {
        return sharedPrefs.getString(PHONENUMBER_KEY, "");
    }

    @Override
    public User getUser() {
        String serializedUser = sharedPrefs.getString(USER_KEY, "");

        if (!StringUtils.isNullOrEmpty(serializedUser)) {
            return new Gson().fromJson(serializedUser, User.class);
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        String serializedUser = new Gson().toJson(user);

        preferencesEditor.putString(USERNAME_KEY, user.getUsername());
        preferencesEditor.putString(EMAIL_KEY, user.getEmail().getEmail());
        preferencesEditor.putString(PHONENUMBER_KEY, user.getPhoneNumber().getPhoneNumber());
        preferencesEditor.putString(USER_KEY, serializedUser);
        preferencesEditor.commit();
    }
}
