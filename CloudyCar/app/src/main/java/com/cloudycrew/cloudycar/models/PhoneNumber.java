package com.cloudycrew.cloudycar.models;

/**
 * Created by George on 2016-10-13.
 */
public class PhoneNumber {

    private String phoneNumber;

    /**
     * Instantiates a new Phone number.
     *
     * @param number the number
     */
    public PhoneNumber(String number) {
        this.setPhoneNumber(number);
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     * @throws InvalidPhoneNumberException if the number is invalid
     */
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

    /**
     * Returns a human-readable representation of a PhoneNumber
     *
     * @return the string
     */
    public String prettyPrint() {
        return String.format("(%s)-%s-%s",
                phoneNumber.substring(0, 3),
                phoneNumber.substring(3, 6),
                phoneNumber.substring(6));
    }

    public static class InvalidPhoneNumberException extends RuntimeException {

    }

}
