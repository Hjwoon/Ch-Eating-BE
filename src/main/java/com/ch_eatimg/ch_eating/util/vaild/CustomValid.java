package com.ch_eatimg.ch_eating.util.vaild;

import java.util.regex.Pattern;

public class CustomValid {
    static String userIdRegex = "^[a-zA-Z0-9]+$";

    public static boolean isUserIdValid(String userId) {
        return Pattern.compile(userIdRegex).matcher(userId).matches();
    }
}