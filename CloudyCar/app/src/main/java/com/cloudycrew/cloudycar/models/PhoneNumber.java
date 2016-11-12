package com.cloudycrew.cloudycar.models;

/**
 * Created by George on 2016-10-13.
 */

public class PhoneNumber {

    private String phoneNumber;

    public PhoneNumber(String number) {
        this.setPhoneNumber(number);
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException
    {
        if (!validate(phoneNumber))
        {
            throw new InvalidPhoneNumberException();
        }
    }


    private boolean validate(String phoneNumber)
    {
        String validatedNumber = "";
        for (char c: phoneNumber.toCharArray())
        {
            if(Character.isDigit(c))
            {
                validatedNumber += c;
            }
        }
        //We only support normal phone numbers or something....
        if (validatedNumber.length() < 10)
        {
            return false;
        }
        this.phoneNumber = validatedNumber;
        return true;
    }

    public String prettyPrint() {
        return String.format("(%s)-%s-%s",
                phoneNumber.substring(0, 3),
                phoneNumber.substring(3, 6),
                phoneNumber.substring(6));
    }

    public static class InvalidPhoneNumberException extends RuntimeException {

    }

}
