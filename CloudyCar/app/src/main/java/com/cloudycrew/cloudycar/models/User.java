package com.cloudycrew.cloudycar.models;

import com.cloudycrew.cloudycar.Identifiable;
import com.cloudycrew.cloudycar.email.Email;

/**
 * Created by George on 2016-10-13.
 */

public class User implements Identifiable {
    private String username;
    private Email email;
    private PhoneNumber phoneNumber;
    private String firstName;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getId() {
        return getUsername();
    }
}
