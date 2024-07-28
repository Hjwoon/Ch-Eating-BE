package com.ch_eatimg.ch_eating.util.vaild;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final String USER_ID_REGEX = "^[a-zA-Z0-9_-]{6,12}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%])[a-zA-Z0-9!@#$%]{8,15}$";

    public static boolean isUserIdValid(String userId) {
        return Pattern.matches(USER_ID_REGEX, userId);
    }

    public static boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
