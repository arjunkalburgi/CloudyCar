package com.cloudycrew.cloudycar;

import android.provider.ContactsContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import java.util.Arrays;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Ryan on 2016-10-12.
 */

public class UserProfileTests
{
    private IUserService userService;
    private User completeUser;
    private User incompleteUser;
    private PhoneNumber phoneNumber;
    private Email email;

    @Before
    public void set_up() {
        phoneNumber = new PhoneNumber("780-2676-364"); //Corndog
        email = new Email("wapoz@george.moe");
        contactInfo = new contactInfo(phoneNumber, email);
        completeUser = new User("janedoedoe");
        completeUser.setEmail(email);
        completeUser.setPhoneNumber(phoneNumber);
        incompleteUser = new User("I'm sad and have no contact info");
    }

    @Test
    public void test_addNewUser_withAnUnusedUserName_andTestGettingUserFromUsernameOnly() {
        userService.add(completeUser);
        assertTrue(completeUser, userService.getUser("janedoedoe"));
    }

    @Test(expected=DuplicateUserException.class)
    public void test_addNewUser_withAUsedUserName_throwingDuplicateUserException() {
        userService.add(completeUser);
        userService.add(completeUser);
    }

    @Test(expected=IncompleteUserException.class)
    public void test_addNewUser_withIncompleteInformation_throwingIncompleteUserException() {
        userService.add(incompleteUser);
    }

    @Test(expected=InvalidEmailException.class)
    public void test_EditingUserEmailWithInvalidEmail_throwingInvalidEmailException() {
        completeUser.setEmail(new Email("notarealemail"));
    }

    @Test(expected=InvalidPhoneNumberException.class)
    public void test_EditingUserPhoneNumber_throwingInvalidPhoneNumberException() {
        completeUser.setPhoneNumber(new PhoneNumber("corndog")); //Corndog
    }

    @Test
    public void test_EditingUserPhoneNumber() {
        PhoneNumber newPhone = new PhoneNumber("780-1111-222");
        completeUser.setPhoneNumber(newPhone);
        assertEquals(newPhone, completeUser.getPhoneNumber());
    }

    @Test
    public void test_EditingUserEmailAddress() {
        Email newEmail = new Email("abc@test.ca");
        completeUser.setEmail(newEmail);
        assertEquals(email, completeUser.getEmail());
    }

    @Test(expected=UserDoesNotExistException.class)
    public void test_GettingANonExistentUser_throwingUserDoesNotExistException() {
        userService.get("I don't exist");
    }

}
