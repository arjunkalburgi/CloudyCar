package com.cloudycrew.cloudycar.users;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cloudycrew.cloudycar.models.User;

/**
 * Created by Ryan on 2016-11-10.
 */

public class UserPreferences {
    public static final String USERNAME_KEY = "user_name";
    public static final String EMAIL_KEY = "email";
    public static final String PHONENUMBER_KEY = "phone_number";

    private static final String USER_PREFS = UserPreferences.class.getSimpleName();
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor preferencesEditor;

    public UserPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.preferencesEditor = sharedPrefs.edit();
    }

    public String getUserName() {
        return sharedPrefs.getString(USERNAME_KEY, "");
    }

    public String getEmail() {
        return sharedPrefs.getString(USERNAME_KEY, "");
    }

    public String getPhoneNumber() {
        return sharedPrefs.getString(USERNAME_KEY, "");
    }

    public void saveUser(User user) {
        preferencesEditor.putString(USERNAME_KEY, user.getUsername());
        preferencesEditor.putString(EMAIL_KEY, user.getEmail().getEmail());
        preferencesEditor.putString(PHONENUMBER_KEY, user.getPhoneNumber().getPhoneNumber());
        preferencesEditor.commit();
    }
}
