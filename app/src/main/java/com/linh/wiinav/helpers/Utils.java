package com.linh.wiinav.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static String convertImageName(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime()) + "_");
        sb.append(userId);
        return sb.toString();
    }
}
