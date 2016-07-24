package org.census.commons.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Common utilities for working with DATE/TIME.
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 14.07.2015)
*/

public final class CommonDatesUtils {

    private CommonDatesUtils() {
        // non-instanceability
    }

    /**
     * Method resets date to day start time (00:00:00.000).
     */
    public static Date resetToDayStart(Date date) {
        if (date == null) {
                throw new IllegalArgumentException("Invalid input date -> null!");
        }
        // reset date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Method resets date to day end time (23:59:59.999).
     */
    public static Date resetToDayEnd(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid input date -> null!");
        }
        // reset date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * Converts time value from milliseconds to string formatted as HH:MM:SS.
     * @param time long time in milliseconds.
     */
    public static String getHHMMSSfromMS(long time) {
        long seconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);
        return String.format("%02d:%02d:%02d", seconds / 3600 /*HH*/, (seconds % 3600) / 60 /*MM*/, (seconds %3600) % 60 /*SS*/);
    }

}