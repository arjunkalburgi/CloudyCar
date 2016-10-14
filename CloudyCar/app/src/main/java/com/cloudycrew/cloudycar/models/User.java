package com.cloudycrew.cloudycar.models;

import com.cloudycrew.cloudycar.email.Email;

/**
 * Created by George on 2016-10-13.
 */

public class User {
    private Email email;
    private PhoneNumber phoneNumber;
    private String firstName;

    public User(String username) {

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

}
