package gr.university.dscc.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * this class is used as a gr.university.dscc.util class to handle everything related to time in general, calculations between dates etc.
 * will be done here and used 'globally' (statically)
 */
public class Time {


    /**
     * this method takes a take and returns it as a String in the yyyy-MM-dd format
     *
     * @param date : the date that needs to be formatted
     * @return : returns a String from the date inputted
     */
    public static String returnYYYYMMDDString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * this method sets the clock of a date to zero, mainly used to group dates together
     *
     * @param date: the date that the user requested to set the clock to zero
     * @return : returns a date with its clock set to 00:00:00
     */
    public static Date setClockToZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //we set the end date's clock to  00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * this method sets the clock of a date to max, mainly used to group dates together
     *
     * @param date: the date that the user requested to set the clock to max
     * @return : returns a date with its clock set to 29:59:59
     */
    public static Date setClockToMax(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //we set the end date's clock to  00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    /**
     * this method takes two strings and if they are parsable, transforms them to Date objects, if they are not
     * parsable, then it returns a null for any date that was not parsed
     *
     * @param fromDateStr : the from date string we want to format
     * @param toDateStr   : the to date string we want to format
     * @return : returns a Date Array which contains the fromDate and toDate objects
     */
    public static Date[] handleDates(String fromDateStr, String toDateStr) {
        Date[] dateRange = new Date[2];
        Date fromDate = null, toDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (fromDateStr != null) {
            try {
                LocalDate date = LocalDate.parse(fromDateStr, formatter);
                //converting to what SQL requests
                fromDate = java.sql.Date.valueOf(date);
                //to take from the start of the day
                fromDate = Time.setClockToZero(fromDate);
            }
            //if it is wrong format, then do not count as a date
            catch (Exception e) {
                fromDate = null;
            }
        }
        if (toDateStr != null) {
            try {
                LocalDate date2 = LocalDate.parse(toDateStr, formatter);
                //converting to what SQL requests
                toDate = java.sql.Date.valueOf(date2);
                //need to take from the end of the date
                toDate = Time.setClockToMax(toDate);
                dateRange[1] = toDate;
            }
            //if it is wrong format, then do not count as a date
            catch (Exception e) {
                fromDate = null;
            }
        }
        dateRange[0] = fromDate;
        return dateRange;
    }
}
