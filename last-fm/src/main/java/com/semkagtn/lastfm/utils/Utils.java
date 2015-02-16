package com.semkagtn.lastfm.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by semkagtn on 2/15/15.
 */
public class Utils {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    private Utils() {

    }
}
