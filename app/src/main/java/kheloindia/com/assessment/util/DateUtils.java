package kheloindia.com.assessment.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by PC10 on 29-May-18.
 */

public class DateUtils {

    private static String  HOUR_FORMAT = "HH:mm";

    public static String getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfHour = new SimpleDateFormat(HOUR_FORMAT);
        String hour = sdfHour.format(cal.getTime());
        return hour;
    }

    /**
     * @param  target  hour to check
     * @param  start   interval start
     * @param  end     interval end
     * @return true    true if the given hour is between
     */
    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0)
                && (target.compareTo(end) <= 0));
    }
}
