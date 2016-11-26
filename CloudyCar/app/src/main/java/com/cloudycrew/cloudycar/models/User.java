package com.cloudycrew.cloudycar.models;

import com.cloudycrew.cloudycar.Identifiable;
import com.cloudycrew.cloudycar.email.Email;
import com.cloudycrew.cloudycar.models.phonenumbers.PhoneNumber;

/**
 * Created by George on 2016-10-13.
 */
public class User implements Identifiable {
    private String username;
    private Email email;
    private PhoneNumber phoneNumber;
    private String firstName;
    private String carDescription;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Verify contact information boolean.
     *
     * @return the boolean
     */
    public boolean verifyContactInformation() {
        return this.email != null && this.phoneNumber != null;
    }


    /**
     * Gets car description.
     *
     * @return the carDescription
     */
    public String getCarDescription() {
        return carDescription;
    }

    /**
     * Sets car description.
     *
     * @param carDescription the first name
     */
    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }
    /**
     * Verify car description boolean.
     *
     * @return the boolean
     */
    public boolean hasCarDescription() {
        return this.carDescription != null;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null)
            return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(user.getPhoneNumber()) : user.getPhoneNumber() != null)
            return false;
        return getFirstName() != null ? getFirstName().equals(user.getFirstName()) : user.getFirstName() == null;

    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        return result;
    }
}
