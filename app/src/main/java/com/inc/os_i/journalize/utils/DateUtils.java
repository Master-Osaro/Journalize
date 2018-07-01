package com.inc.os_i.journalize.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getSimpleDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        final String formattedDate = dateFormat.format(date).toString();
        return formattedDate;
    }
}
