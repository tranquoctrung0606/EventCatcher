package com.linh.wiinav.helpers;

import android.text.TextUtils;

public class ValidationHelper
{
    public static boolean isEmptyField(final String fieldContent) {
        return TextUtils.isEmpty(fieldContent);
    }

    public static boolean isValidEmail(final String emailContent) {
        return true;
    }

    public static boolean isValidPassword(final String passwordContent) {
        return true;
    }
}
