package com.linh.wiinav.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String convertImageName(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime()) + "_");
        sb.append(userId);
        return sb.toString();
    }

    public static String getTiming(Date date) {
        Date currentDate = Calendar.getInstance().getTime();
        if (date.getYear() == currentDate.getYear()) {
            if (date.getMonth() == currentDate.getMonth()) {
                if (date.getDay() == currentDate.getDay()) {
                    if (date.getHours() == currentDate.getHours()) {
                        if (date.getMinutes() == currentDate.getMinutes()) {
                            if (currentDate.getSeconds() - date.getSeconds() <= 10) {
                                return "Just now";
                            } else {
                                return currentDate.getSeconds() - date.getSeconds() + "s ago";
                            }
                        } else {
                            return currentDate.getMinutes() - date.getMinutes() + "m ago";
                        }
                    } else {
                        return currentDate.getHours() - date.getHours() + "h ago";
                    }
                } else {
                    return currentDate.getDay() - date.getDay() + "d ago";
                }
            } else {
                return currentDate.getMonth() - date.getMonth() + "m ago";
            }
        }else {
            return currentDate.getYear() - date.getYear() + "y ago";
        }
    }
}
