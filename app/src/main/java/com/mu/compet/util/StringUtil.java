package com.mu.compet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mu on 2016-10-21.
 */

public class StringUtil {

    public static boolean isEmpty(String str) {
        if(str == null || str.trim().length() ==0)
            return true;

        return false;
    }

    public static boolean isAnyEmpty(String str1, String str2) {
        if(isEmpty(str1) || isEmpty(str2))
            return true;

        return false;
    }

    public static boolean isAllEmpty(String str1, String str2) {
        if(isEmpty(str1) && isEmpty(str2))
            return true;

        return false;
    }

    public static String calculateDate(String strDate) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmm", Locale.KOREA);
        Date startDate;
        Date endDate;
        long duration;
        String calDate = null;

        try {
            startDate = dateFormat.parse(strDate);
            endDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
            duration = (endDate.getTime() - startDate.getTime()) / 1000;

            if (duration / (60 * 60 * 24) == 0) {
                duration = duration / (60 * 60);
                calDate = duration + "시간 전";
            } else {
                duration = duration / (60 * 60 * 24);
                calDate = duration + "일 전";

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calDate;
    }
}