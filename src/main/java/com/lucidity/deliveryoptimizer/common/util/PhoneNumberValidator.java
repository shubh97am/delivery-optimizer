package com.lucidity.deliveryoptimizer.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final Pattern VALID_PHONE_REGEX = Pattern.compile("^(?:\\+[1-9]{1,3})[0-9]{3,14}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_INDIA_PHONE_REGEX = Pattern.compile("^(?:\\+91)([6-9]{1})\\d{9}$", Pattern.CASE_INSENSITIVE);

    public static boolean validPhone(String phone) {
        Matcher matcher = null;
        matcher = VALID_INDIA_PHONE_REGEX.matcher(phone);
        return matcher.find();
    }

    public static void main(String[] args) {
        String phone = "+919232292881";
        validPhone(phone);
    }
}
