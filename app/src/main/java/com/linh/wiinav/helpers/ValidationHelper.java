package com.linh.wiinav.helpers;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper
{
    public static boolean isEmptyField(final String fieldContent) {
        return TextUtils.isEmpty(fieldContent);
    }

    public static boolean isValidEmail(final String emailContent) {
        String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]" +
                "+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)" +
                "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emailContent);
        return matcher.matches();
    }

    public static boolean isValidPassword(final String passwordContent) {
        if(passwordContent!=null && passwordContent.length()>=6){
            return true;
        }
        else{
            return false;
        }
    }
}
