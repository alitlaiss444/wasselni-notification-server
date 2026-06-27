package com.wasselni.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.util.IslamicCalendar;
import com.ibm.icu.util.ULocale;
import com.wasselni.common.model.expections.BackendException;

/**
 * Utility functions for {@link Date} handling
 * 
 * @author Wassim
 *
 */
public class DateUtils {

	/**
	 * Gets a date as a string in the format yyyyMMdd
	 * 
	 * @param date date to convert
	 * @return the string value of the date in the format yyyyMMdd. One space is
	 *         returned in case of any exception
	 */
	public static String getSerialDate(Date date) {
		try {
			return new SimpleDateFormat("yyyyMMdd").format(date);
		} catch (Exception e) {
			return " ";
		}
	}

	/**
	 * Gets the current date/time in Long format as a string
	 * 
	 * @return the current time as a string
	 */
	public static String getNowInUtilLong() {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * Gets the current date as a string in the format yyyyMMdd
	 * 
	 * @return the string value of the current date in the format yyyyMMdd.
	 */
	public static String getSerialDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * Gets the current time as a string in the format HHmm
	 * 
	 * @return the formatted current time in the format HHmm
	 */
	public static String getSerialTime() {
		return new SimpleDateFormat("HHmm").format(new Date());
	}

	/**
	 * Parses the string passed date using the format passed to it
	 * 
	 * @param date   date to parse
	 * @param format format to use for format the date
	 * @return {@link Date} instance generated from the date string passed using the
	 *         format specified. In case of any exception, null is returned
	 */
	public static Date parseDate(String date, String format) {

		if (StringUtils.isBlank(date)) {
			return null;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Parses the string passed date using the format passed to it
	 * 
	 * @param date         date to parse
	 * @param format       format to use for format the date
	 * @param defaultValue default date to return in case the input is null or
	 *                     invalid
	 * @return {@link Date} instance generated from the date string passed using the
	 *         format specified. In case of any exception, null is returned
	 */
	public static Date parseDate(String date, String format, Date defaultValue) {

		if (StringUtils.isBlank(date)) {
			return defaultValue;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (ParseException e) {
			return defaultValue;
		}
	}

	/**
	 * Parses the string passed date using the format passed to it Parsing by
	 * eliminating time zone ambiguity
	 * 
	 * @param date         date to parse
	 * @param format       format to use for format the date
	 * @param defaultValue default date to return in case the input is null or
	 *                     invalid
	 * @return {@link Date} instance generated from the date string passed using the
	 *         format specified. In case of any exception, null is returned
	 */
	public static Date parseDateWithUTCTimeZone(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Gets the current date with no time
	 * 
	 * @return the current date with the value of the time as 0
	 */
	public static Date getCurrentDateNoTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Gets the current time as long in the format HHmmss
	 * 
	 * @return the long value of the current time in the format HHmmss
	 */
	public static Long getCurrentTimeAsLong() {

		return Long.valueOf(new SimpleDateFormat("HHmmss").format(new Date()));
	}

	/**
	 * Gets the time as long from the specified date in the format HHmmss
	 * 
	 * @param date date to get the time from
	 * @return time in HHmmss format. If the input date is null, null is returned
	 */
	public static Long getTimeAsLongFromDate(Date date) {
		if (date == null)
			return null;

		return Long.valueOf(new SimpleDateFormat("HHmmss").format(date));
	}

	/**
	 * Gets the current date as time stamp
	 * 
	 * @return the current time stamp
	 */
	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Converts a time stamp to a string in the format yyyyMMdd-HHmmss.SSS
	 * 
	 * @param timestamp time stamp to convert
	 * @return formatted time stamp, empty string if the time stamp specified is
	 *         null
	 */
	public static String timeStampToString(Timestamp timestamp) {

		if (timestamp == null)
			return "";

		return new SimpleDateFormat("yyyyMMdd-HHmmss.SSS").format(timestamp.getTime());
	}

	/**
	 * Converts the string provided to a time stamp object
	 * 
	 * @param timeStampString time stamp string to convert in the format
	 *                        yyyyMMdd-HHmmss.SSS
	 * @return time stamp instance from the specified object, null in case the
	 *         string is invalid or empty
	 */
	public static Timestamp stringToTimeStamp(String timeStampString) {

		if (StringUtils.isBlank(timeStampString))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
			sdf.setLenient(false);
			return new Timestamp(sdf.parse(timeStampString).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converts a date and time entries to timestamp
	 * 
	 * @param date date to convert to timestamp
	 * @param time time to convert to timestamp
	 * @return Date and time converted to timestamp. If date is null, null is
	 *         returned
	 */
	public static Timestamp dateTimeToTimestamp(Date date, long time) {

		if (date == null)
			return null;

		String dateString = new SimpleDateFormat("yyyyMMdd").format(date);
		String timeString = StringUtils.leftPad(String.valueOf(time), 6, "0");

		return stringToTimeStamp(dateString + "-" + timeString + ".000");

	}

	/**
	 * Resets the time from a date
	 * 
	 * @param date date to reset the time from
	 * @return date without time (00:00:00 time). If the input date is null, null is
	 *         returned
	 */
	public static Date resetTimeFromDate(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Removes the time from the specified date
	 * 
	 * @param date date to remove the time from
	 * @return date without time
	 * @deprecated use {@link #resetTimeFromDate(Date)} instead.
	 */
	public static Date resetHoursFromDate(Date date) {
		return resetTimeFromDate(date);
	}

	/**
	 * Gets the date with the day as the last day of the month of the specified date
	 * 
	 * @param date date to get the last day of the month from
	 * @return an instance of {@link Date} with the day as the last day of the month
	 *         of the passed date. The returned date will not have a time set in it.
	 *         If the date specified is null, null is returned
	 */
	public static Date getLastDayOfMonth(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Gets the date with the day as the last day of the week of the specified date
	 * 
	 * @param date date to get the last day of the week from. If set as null, the
	 *             value returned is null
	 * @return an instance of {@link Date} with the day as the last day of the week
	 *         of the passed date. The returned date will not have a time set in it.
	 */
	public static Date getLastDayOfWeek(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.add(Calendar.DATE, -1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Gets the date with the day as the last day of the year of the specified date
	 * 
	 * @param date date to get the last day of the year from. If specified as null,
	 *             null will be returned
	 * @return an instance of {@link Date} with the day as the last day of the year
	 *         of the passed date. The returned date will not have a time set in it
	 */
	public static Date getLastDayOfYear(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.add(Calendar.DATE, -1);

		return resetTimeFromDate(calendar.getTime());

	}

	/**
	 * Gets the date with the day as the first day of the week of the specified date
	 * 
	 * @param date date to get the first day of the week from. If set to null, null
	 *             is returned
	 * @return an instance of {@link Date} with the day as the first day of the week
	 *         of the passed date. The returned date will not have a time set in it
	 */
	public static Date getFirstDayOfWeek(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Sets the date to the day of week specified
	 * 
	 * @param date      date to set the day of week in. If specified as null, null
	 *                  is returned
	 * @param dayOfWeek day of week to set in the date (1 being sunday and 7 sat)
	 * @return {@link Date} with the day as the day of the week specified
	 */
	public static Date setDayOfWeek(Date date, int dayOfWeek) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

		return resetTimeFromDate(calendar.getTime());

	}

	/**
	 * Gets the date with the day as the first day of the month of the specified
	 * date
	 * 
	 * @param date date to get the first day of the month from
	 * @return an instance of {@link Date} with the day as the first day of the
	 *         month of the passed date. The returned date will not have a time set
	 *         in it. If the input date specified is null, null is returned
	 */
	public static Date getFirstDayOfMonth(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Gets the date with the day as the first day of the month of the specified
	 * date
	 * 
	 * @param date date to get the first day of the month from
	 * @return an instance of {@link Date} with the day as the first day of the
	 *         month of the passed date. The returned date will not have a time set
	 *         in it. If the input date specified is null, null is returned
	 */
	public static Date setDayOfMonth(Date date, int dayOfMonth) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Gets the date with the day as the first day of the year of the specified date
	 * 
	 * @param date date to get the first day of the year from
	 * @return an instance of {@link Date} with the day as the first day of the year
	 *         of the passed date. The returned date will not have a time set in it.
	 *         If the input date specified is null, null is returned
	 */
	public static Date getFirstDayOfYear(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_YEAR, 1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Adds the specified number of days to the specified date
	 * 
	 * @param date    date to add the number of days to
	 * @param numDays number of days to add to the specified date. if set to a
	 *                negative value, the days are subtracted from the specified
	 *                date
	 * @return an instance of {@link Date} with the added/subtracted days. The
	 *         returned date will not have the time set in it. If the input date is
	 *         null, null is returned
	 */
	public static Date addDaysToDate(Date date, int numDays) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.DATE, numDays);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Adds the specified number of weeks to the specified date. The date returned
	 * is the day the weeks starting the provided date are complete and not the
	 * first day after which the weeks are completed
	 * 
	 * @param date     date to add the number of weeks to
	 * @param numWeeks number of weeks to add to the specified date. if set to a
	 *                 negative value, the weeks are subtracted from the specified
	 *                 date
	 * @return an instance of {@link Date} with the added/subtracted weeks. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addWeeksToDate(Date date, int numWeeks) {

		return addWeeksToDate(date, numWeeks, true);
	}

	/**
	 * Adds the specified number of weeks to the specified date.
	 * 
	 * @param date     date to add the number of weeks to
	 * @param numWeeks number of weeks to add to the specified date. if set to a
	 *                 negative value, the weeks are subtracted from the specified
	 *                 date
	 * @param lastDay  specifies if the required date is the last date of completion
	 *                 of the weeks or the one after it
	 * @return an instance of {@link Date} with the added/subtracted weeks. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addWeeksToDate(Date date, int numWeeks, boolean lastDay) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.WEEK_OF_YEAR, numWeeks);
		if (lastDay)
			calendar.add(Calendar.DAY_OF_WEEK, -1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Adds the specified number of months to the specified date. The date returned
	 * is the day the months starting the provided date are complete and not the
	 * first day after which the months are completed
	 * 
	 * @param date      date to add the number of months to
	 * @param numMonths number of months to add to the specified date. if set to a
	 *                  negative value, the months are subtracted from the specified
	 *                  date
	 * @return an instance of {@link Date} with the added/subtracted months. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addMonthsToDate(Date date, int numMonths) {

		return addMonthsToDate(date, numMonths, true);
	}

	/**
	 * Adds the specified number of months to the specified date.
	 * 
	 * @param date      date to add the number of months to
	 * @param numMonths number of months to add to the specified date. if set to a
	 *                  negative value, the months are subtracted from the specified
	 *                  date
	 * @param lastDay   specifies if the required date is the last day of completion
	 *                  of the months or the one after it
	 * @return an instance of {@link Date} with the added/subtracted months. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addMonthsToDate(Date date, int numMonths, boolean lastDay) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.MONTH, numMonths);
		if (lastDay)
			calendar.add(Calendar.DAY_OF_MONTH, -1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Adds the specified number of years to the specified date. The date returned
	 * is the day the years starting the provided date are complete and not the
	 * first day after which the years are completed
	 * 
	 * @param date     date to add the number of years to
	 * @param numYears number of years to add to the specified date. if set to a
	 *                 negative value, the years are subtracted from the specified
	 *                 date
	 * @return an instance of {@link Date} with the added/subtracted years. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addYearsToDate(Date date, int numYears) {

		return addYearsToDate(date, numYears, true);
	}

	/**
	 * Adds the specified number of years to the specified date.
	 * 
	 * @param date     date to add the number of years to
	 * @param numYears number of years to add to the specified date. if set to a
	 *                 negative value, the years are subtracted from the specified
	 *                 date
	 * @param lastDay  specifies if the required date is the last day of completion
	 *                 of the years or the one after it
	 * @return an instance of {@link Date} with the added/subtracted years. The
	 *         returned date will not have the time set in it. If the input date
	 *         specified is null, null is returned
	 */
	public static Date addYearsToDate(Date date, int numYears, boolean lastDay) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.YEAR, numYears);
		if (lastDay)
			calendar.add(Calendar.DAY_OF_YEAR, -1);

		return resetTimeFromDate(calendar.getTime());
	}

	/**
	 * Adds the specified number of seconds to the specified date
	 * 
	 * @param date       date to add the number of seconds to
	 * @param numSeconds number of seconds to add to the specified date. if set to a
	 *                   negative value, the seconds are subtracted from the
	 *                   specified date
	 * @return an instance of {@link Date} with the added/subtracted seconds.If the
	 *         input date specified is null, null is returned
	 */
	public static Date addSecondsToDate(Date date, int numSeconds) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.SECOND, numSeconds);

		return calendar.getTime();

	}

	/**
	 * Checks if 2 dates are equal - dates will be converted to char and compared to
	 * avoid having difference in hours/mins/sec
	 * 
	 * @param date1 first date to compare
	 * @param date2 second date to compare
	 * @return true if the dates are equal (excluding the time) false otherwise. If
	 *         any of the dates specified is null, false is returned. If both dates
	 *         are null, true is returned
	 */
	public static boolean dateEqual(Date date1, Date date2) {

		if (date1 == null && date2 == null)
			return true;
		else if (date1 == null || date2 == null)
			return false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date1String = sdf.format(date1);
		String date2String = sdf.format(date2);

		return date1String.equals(date2String);
	}

	/**
	 * Checks if a date is between the specified date range inclusive. The time is
	 * not taken into consideration in the check. Purely the date is checked
	 * 
	 * @param checkDate date to check if in the range
	 * @param fromDate  start date of the range
	 * @param toDate    end date of the range
	 * @return true if the date specified is within the range inclusive
	 */
	public static boolean dateBetweenInclusive(Date checkDate, Date fromDate, Date toDate) {

		return dateAfterOrEqual(checkDate, fromDate) && dateBeforeOrEqual(checkDate, toDate);
	}

	/**
	 * Checks if date 1 is after or equal to date 2. If either date 1 or 2 is null,
	 * false is returned. The time is not taken into consideration in the check.
	 * Only the date purely is checked
	 * 
	 * @param date1 date 1 to check if equal or after date 2
	 * @param date2 date 2 to use in the check
	 * @return true if date 1 is after or equal to date 2, false otherwise. If
	 *         either date 1 or 2 is null, false is returned
	 */
	public static boolean dateAfterOrEqual(Date date1, Date date2) {

		if (date1 == null || date2 == null)
			return false;

		if (DateUtils.resetTimeFromDate(date1).after(DateUtils.resetTimeFromDate(date2))
				|| DateUtils.dateEqual(date1, date2))
			return true;

		return false;
	}

	/**
	 * Checks if date 1 is before or equal to date 2. If either date 1 or 2 is null,
	 * false is returned. The time is not taken into consideration in the check.
	 * Only the date purely is checked
	 * 
	 * @param date1 date 1 to check if equal or before date 2
	 * @param date2 date 2 to use in the check
	 * @return true if date 1 is before or equal to date 2, false otherwise. If
	 *         either date 1 or 2 is null, false is returned
	 */
	public static boolean dateBeforeOrEqual(Date date1, Date date2) {

		if (date1 == null || date2 == null)
			return false;

		if (DateUtils.resetTimeFromDate(date1).before(DateUtils.resetTimeFromDate(date2))
				|| DateUtils.dateEqual(date1, date2))
			return true;

		return false;
	}

	/**
	 * Format the date passed to it for debug in the format yyyy-MM-dd
	 * 
	 * @param date date to format.
	 * @return formatted string date in the format yyyy-MM-dd. Empty string in case
	 *         the date parameter is null
	 */
	public static String formatDateForDisplay(Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * Formats the date passed to it for debugs including the time
	 * 
	 * @param date date to format for display
	 * @return formatted date/time. An empty string is returned in case the date
	 *         parameter is null
	 */
	public static String formatDateTimeForDisplay(Date date) {

		if (date == null)
			return "";

		return new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss").format(date);
	}

	/**
	 * Gets the difference in hours between 2 dates
	 * 
	 * @param date1 first date to use to get the difference
	 * @param date2 second date to use to get the difference
	 * @return number of hours between the specified 2 dates. The value is always
	 *         positive regardless if date 1 is greater than date 2 and vice-versa
	 */
	public static long getDifferenceInHours(Date date1, Date date2) {

		return getDifferenceInMinutes(date1, date2) / 60;
	}

	/**
	 * Gets the difference in minutes between 2 dates
	 * 
	 * @param date1 first date to use to get the difference
	 * @param date2 second date to use to get the difference
	 * @return number of minutes between the specified 2 dates. The value is always
	 *         positive regardless if date 1 is greater than date 2 and vice-versa
	 */
	public static long getDifferenceInMinutes(Date date1, Date date2) {

		if (date2.after(date1)) {
			return (date2.getTime() / 60000) - (date1.getTime() / 60000);
		} else {
			return (date1.getTime() / 60000) - (date2.getTime() / 60000);
		}

	}

	/**
	 * Add/Subtracts 2 dates and return the difference in days. The number of days
	 * returned might be negative if @param dateRigth is less than @param dateLeft
	 * 
	 * @param dateLeft  first date to use to get the days difference
	 * @param dateRight second date to use to get the days difference
	 * @return the number of difference in days between the 2 specified dates. if
	 *         dateLeft is less than dateRight, the returned value is negative
	 */
	public static int addSubstractDatesReturnDays(Date dateLeft, Date dateRight) {

		long timeDifference = dateLeft.getTime() - dateRight.getTime();

		return (int) (timeDifference / (24 * 60 * 60 * 1000));
	}

	/**
	 * Add/Subtract months to a Date then gets the last day of the month of the
	 * result. Usually used to get the expiry date which is the last day of a
	 * specified month
	 * 
	 * @param date   date to add the months to
	 * @param months number of months to add to the date
	 * @return date with the added months and having the day as the last day of the
	 *         resulting date month
	 */
	public static Date addSubstractMonthMaxDay(Date date, int months) {
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, months);
		gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		return gc.getTime();
	}

	/**
	 * Add/Subtract months to a Date then gets the last day of the month of the
	 * result. Usually used to get the expiry date which is the last day of a
	 * specified month and set the month similar to the date provided
	 * 
	 * @param date      date to add the months to
	 * @param months    number of months to add to the date
	 * @param monthDate date to get the current month of
	 * @return date with the added months and having the day as the last day of the
	 *         resulting date month
	 */
	public static Date addSubstractMonthMaxDaySameMonth(Date date, int months, Date monthDate) {
		Calendar gc = Calendar.getInstance();
		Calendar gc2 = Calendar.getInstance();
		gc.setTime(date);
		gc2.setTime(monthDate);
		gc.add(Calendar.MONTH, months);
		gc.set(Calendar.MONTH, gc2.get(Calendar.MONTH));
		gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		return gc.getTime();
	}

	/**
	 * Checks if the year of the specified date is leap
	 * 
	 * @param date date to check the year from
	 * @return true if the year is leap, false otherwise. If the date specified is
	 *         null, false is returned
	 */
	public static boolean isLeapYear(Date date) {

		if (date == null)
			return false;

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR));

	}

	/**
	 * Gets the number of millis until end of year of the specified date
	 *
	 * @param date date to use for the calculation
	 * @return the number of millis starting the specified date until the end of
	 *         year. If null is specified, 0 is returned
	 */
	public static long getNumberOfMillisToEndOfYear(Date date) {

		if (date == null)
			return 0L;

		Date endOfYear = getLastDayOfYear(date);
		return endOfYear.getTime() - date.getTime() + 1;
	}

	/**
	 * Gets the number of millis between 2 dates
	 *
	 * @param startDate start date
	 * @param endDate   end date
	 * @return millis between the 2 specified dates. if the endDate is less than the
	 *         startDate, the value returned is negative. If either start or end
	 *         date is null, 0 is returned
	 */
	public static long getMillisBetweenDates(Date startDate, Date endDate) {

		if (startDate == null || endDate == null)
			return 0;

		return endDate.getTime() - startDate.getTime() + 1;
	}

	/**
	 * Gets the number of days in the year of the specified date
	 * 
	 * @param date date to get the number of days in
	 * @return the number of days in the year of the specified date
	 */
	public static int getNumberOfDaysInYear(Date date) {

		if (isLeapYear(date)) {
			return 366;
		}
		return 365;
	}

	/**
	 * Gets the year yyyy of the specified date as integer
	 * 
	 * @param date date to get the date for
	 * @return year of the specified date. In case date specified is null, 0 is
	 *         returned
	 */
	public static int getYearOfDate(Date date) {

		if (date == null)
			return 0;

		return Integer.valueOf(new SimpleDateFormat("yyyy").format(date));
	}

	/**
	 * Gets the month mm of the specified date as an integer
	 * 
	 * @param date date to get the month for
	 * @return month of the specified date. In case date specified is null, 0 is
	 *         returned
	 */
	public static int getMonthOfDate(Date date) {

		if (date == null)
			return 0;

		return Integer.valueOf(new SimpleDateFormat("MM").format(date));
	}

	/**
	 * Gets the day of the specified date as an integer
	 * 
	 * @param date date to get the day for
	 * @return day of the specified date. In case date specified is null, 0 is
	 *         returned
	 */
	public static int getDayOfDate(Date date) {

		if (date == null)
			return 0;

		return Integer.valueOf(new SimpleDateFormat("dd").format(date));
	}

	/**
	 * Generates a date from the year/month and day specified
	 * 
	 * @param year  year to set for the date
	 * @param month month to set for the date
	 * @param day   day to set for the date
	 * @return generated date, null in case any of the parameters is out of range
	 */
	public static Date generateDate(int year, int month, int day) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			return sdf.parse(String.valueOf((year * 10000 + month * 100 + day)));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the difference between 2 dates in human readable format
	 * 
	 * @param date1 date 1
	 * @param date2 date 2
	 * @return human readable string showing the difference. If either is null, an
	 *         empty string is returned
	 */
	public static String getDifferenceHumanReadable(Date date1, Date date2) {

		if (date1 == null || date2 == null)
			return "";

		long diff = date1.getTime() - date2.getTime();
		if (diff < 0)
			diff *= -1;

		long second = 1000L;
		long minute = 60L * second;
		long hour = 60L * minute;

		// printing output
		String out = String.format("%02d", diff / hour) + " Hour(s) ";
		out += String.format("%02d", (diff % hour) / minute) + " Minute(s) ";
		out += String.format("%02d", (diff % minute) / second) + " Second(s) ";

		return out;
	}

	/**
	 * return previous date of system's date
	 * 
	 * @return date of yesterday
	 * @throws BackendException
	 */
	public static Date getPreviousDate() throws BackendException {
		return addDaysToDate(getCurrentDateNoTime(), -1);
	}

	/**
	 * Converts the string provided to a time stamp object with the provided format
	 * 
	 * @param timeStampString time stamp string to convert
	 * @param format          format to use to convert time stamp
	 * @return time stamp instance from the specified object, null in case the
	 *         string is invalid or empty
	 */
	public static Timestamp stringToTimeStamp(String timeStampString, String format) {

		if (StringUtils.isBlank(timeStampString))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			return new Timestamp(sdf.parse(timeStampString).getTime());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param time
	 * @return
	 */
	public static boolean isTime(String time) {
		try {

			if (StringUtils.isBlank(time))
				return false;

			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("HHmmss").parse(time));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String formatDateForDb(Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	public static String formatDateForCordys(Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat("ddMMyyyy").format(date);
	}

	/**
	 * Format timestamp in the following format ^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)
	 * (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) ([0-3][0-9]) ([0-9]{4})
	 * ([01][0-9]|2[0-3])(:[0-5][0-9]){2} GMT+0300 (AST)$
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestampForPaymentService(Timestamp timestamp) {
		if (timestamp == null)
			return "";

		return new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'ZZZZ '('zzz')'").format(timestamp.getTime());
	}

	public static String convertToHijri(Date gregorianDate) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(gregorianDate);

		IslamicCalendar islamicCalendar = new IslamicCalendar(new ULocale("ar_SA@calendar=islamic"));
		islamicCalendar.setTime(gregorianCalendar.getTime());

		int day = islamicCalendar.get(IslamicCalendar.DATE);
		int month = islamicCalendar.get(IslamicCalendar.MONTH) + 1;
		int year = islamicCalendar.get(IslamicCalendar.YEAR);

		return String.format("%04d-%02d-%02d", year, month, day);
	}

	public static Date convertHijriToGregorian(int hijriYear, int hijriMonth, int hijriDay) {
		IslamicCalendar islamicCalendar = new IslamicCalendar(new ULocale("ar_SA@calendar=islamic-umalqura"));

		islamicCalendar.set(IslamicCalendar.EXTENDED_YEAR, hijriYear);
		islamicCalendar.set(IslamicCalendar.MONTH, hijriMonth - 1);
		islamicCalendar.set(IslamicCalendar.DATE, hijriDay);

		return islamicCalendar.getTime();
	}

	public static Date convertHijriToGregorian(Date date) {
		IslamicCalendar islamicCalendar = new IslamicCalendar(new ULocale("ar_SA@calendar=islamic-umalqura"));

		islamicCalendar.set(IslamicCalendar.EXTENDED_YEAR, getYearOfDate(date));
		islamicCalendar.set(IslamicCalendar.MONTH, getMonthOfDate(date) - 1);
		islamicCalendar.set(IslamicCalendar.DATE, getDayOfDate(date));

		return islamicCalendar.getTime();
	}

	public static Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar dateOfBirthGregorian) {
		GregorianCalendar gregorianCalendar = dateOfBirthGregorian.toGregorianCalendar();

		SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");
		String dateFormatted = formattedDate.format(gregorianCalendar.getTime());
		return DateUtils.parseDate(dateFormatted, "yyyy-MM-dd");
	}

	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {

		if (date == null) {
			return null;
		}

		try {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			DatatypeFactory df = DatatypeFactory.newInstance();
			XMLGregorianCalendar xmlGregorianCalendar = df.newXMLGregorianCalendar(gc);

			return xmlGregorianCalendar;

		} catch (javax.xml.datatype.DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

}
