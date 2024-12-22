package org.hotels.validators;

import java.util.regex.Pattern;

public class CustomerValidator {
    public boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        String regex = "^\\+?[1-9]\\d{1,14}$";
        return Pattern.matches(regex, phoneNumber);
    }

    public boolean isValidEmailCode(String userCode, String code){
        return code.trim().equalsIgnoreCase(userCode.trim());
    }
}
