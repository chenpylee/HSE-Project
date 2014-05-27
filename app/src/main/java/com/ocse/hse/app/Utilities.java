package com.ocse.hse.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by leehaining on 14-5-26.
 */
public class Utilities {
    public static String getCurrentTime()
    {
        Date now=new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        String date = dateFormat.format(now);
        return date;
    }
}
