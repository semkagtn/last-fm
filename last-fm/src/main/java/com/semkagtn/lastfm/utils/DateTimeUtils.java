package com.semkagtn.lastfm.utils;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by semkagtn on 2/15/15.
 */
public class DateTimeUtils {

    private static DateTimeFormatter birthdayDateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");


    public static Integer calculateAge(String birthday) {
        if (birthday == null) {
            return null;
        }
        try {
            DateTime birthdayDateTime = birthdayDateFormatter.parseDateTime(birthday);
            DateTime nowDateTime = DateTime.now();
            return Years.yearsBetween(birthdayDateTime, nowDateTime).getYears();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private DateTimeUtils() {

    }
}
