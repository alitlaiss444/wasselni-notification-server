package com.wasselni.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.nocrala.tools.texttablefmt.Table;

import com.wasselni.common.model.errors.domain.Result;
import com.wasselni.common.utils.format.AmountFormatUtils;
import com.wasselni.common.utils.format.domain.FormatLineParseResult;

import jakarta.xml.bind.JAXBElement;

public class DebugUtils {

	private static final Log log = LogFactory.getLog(DebugUtils.class);

	private static final int maxLineSize = 80;
	private static final int tagValueTagSpace = 40;
	private static final String specialCharStar = "*";
	private static final String specialCharEquals = "=";
	private static final String specialCharDash = "-";

	public static final int lineStartSpaceNumber = 5;

	private static Map<String, Boolean> simpleTypes = new HashMap<String, Boolean>() {
		private static final long serialVersionUID = 1L;

		{
			put(Integer.class.getName(), true);
			put(int.class.getName(), true);
			put(String.class.getName(), true);
			put(BigInteger.class.getName(), true);
			put(BigDecimal.class.getName(), true);
			put(Long.class.getName(), true);
			put(long.class.getName(), true);
			put(Short.class.getName(), true);
			put(short.class.getName(), true);
			put(Float.class.getName(), true);
			put(float.class.getName(), true);
			put(Boolean.class.getName(), true);
			put(boolean.class.getName(), true);
		}
	};

	/**
	 * Prints a line showing the entry to a service. The service name will be shown
	 * with stars
	 * 
	 * @param str value to print in the logs
	 */
	public static void debugEntryService(String str) {
		debugEntryService(Level.INFO, str);
	}

	/**
	 * Prints a line showing the entry to a service with the specified debug level.
	 * The service name will be shown with stars
	 * 
	 * @param debugLevel debug level to use
	 * @param str        value to print in the logs
	 */
	public static void debugEntryService(Level debugLevel, String str) {
		debugSpecial(debugLevel, str, specialCharStar);
	}

	/**
	 * Prints a line showing the entry to a function/section. The service/function
	 * name will be shown in dashes
	 * 
	 * @param str value to print in the logs
	 */
	public static void debugEntryFunction(String str) {
		debugEntryFunction(Level.INFO, str);
	}

	/**
	 * Prints a line showing the entry to a function/section. The service/function
	 * name will be shown in dashes
	 * 
	 * @param debugLevel debug level to use
	 * @param str        value to print in the logs
	 */
	public static void debugEntryFunction(Level debugLevel, String str) {
		debugSpecial(debugLevel, str, specialCharDash);
	}

	/**
	 * Prints a line showing the exit of a service. The service name will be shown
	 * in stars
	 * 
	 * @param str value to print in the logs
	 */
	public static void debugExitService(String str) {

		debugExitService(Level.INFO, str);
	}

	/**
	 * Prints a line showing the exit of a service. The service name will be shown
	 * in stars
	 * 
	 * @param debugLevel debug level to use
	 * @param str        value to print in the logs
	 */
	public static void debugExitService(Level debugLevel, String str) {

		String outLine = "Leaving " + str;
		debugSpecial(debugLevel, outLine, specialCharStar);
	}

	/**
	 * Prints a line showing the exit of a function/section. The function/section
	 * will be shown in dashes
	 * 
	 * @param str value to print in the logs
	 */
	public static void debugExitFunction(String str) {

		debugExitFunction(Level.INFO, str);

	}

	/**
	 * Prints a line showing the exit of a function/section. The function/section
	 * will be shown in dashes
	 * 
	 * @param debugLevel debug level to use
	 * @param str        value to print in the logs
	 */
	public static void debugExitFunction(Level debugLevel, String str) {

		String outLine = "Leaving " + str;
		debugSpecial(debugLevel, outLine, specialCharDash);

	}

	/**
	 * Prints a line showing the exit of a function/section. The function/section
	 * will be shown in stars. The result value will also be printed
	 * 
	 * @param str    service name to print in the logs
	 * @param result result to display in the logs
	 */
	public static void debugExitService(String str, Result result) {

		debugExitService(Level.INFO, str, result);
	}

	/**
	 * Prints a line showing the exit of a function/section. The function/section
	 * will be shown in stars. The result value will also be printed
	 * 
	 * @param debugLevel debug level to use
	 * @param str        service name to print in the logs
	 * @param result     result to display in the logs
	 */
	public static void debugExitService(Level debugLevel, String str, Result result) {

		String outLine = "Leaving " + str;
		debugSpecial(debugLevel, outLine, specialCharStar);
		debugResult(debugLevel, result);
	}

	/**
	 * Prints a line showing the exit of a function/section. THe function/section
	 * will be shown in dashes. The result specified will also be printed
	 * 
	 * @param str
	 */
	public static void debugExitFunction(String str, Result result) {

		debugExitFunction(Level.INFO, str, result);
	}

	/**
	 * Prints a line showing the exit of a function/section. THe function/section
	 * will be shown in dashes. The result specified will also be printed
	 * 
	 * @param debugLevel debug level to use
	 * @param str        string to print
	 * @param result     result of the function to print
	 */
	public static void debugExitFunction(Level debugLevel, String str, Result result) {

		String outLine = "Leaving " + str;
		debugSpecial(debugLevel, outLine, specialCharDash);
		debugResult(debugLevel, result);
	}

	/**
	 * Prints a line showing the exit of a service and prints the time taken to
	 * finish it. Start time should be passed having the time the function was
	 * started. The service name will be printed in stars
	 * 
	 * @param str       service name to use in the logs
	 * @param startTime start time of the service
	 */
	public static void debugExitService(String str, Long startTime) {

		debugExitService(Level.INFO, str, startTime);

	}

	/**
	 * Prints a line showing the exit of a service and prints the time taken to
	 * finish it. Start time should be passed having the time the function was
	 * started. The service name will be printed in stars
	 * 
	 * @param debugLevel debug level to use
	 * @param str        service name to use in the logs
	 * @param startTime  start time of the service
	 */
	public static void debugExitService(Level debugLevel, String str, Long startTime) {

		String outLine = "Leaving " + str + "[" + (System.currentTimeMillis() - startTime) + "] ms";
		debugSpecial(debugLevel, outLine, specialCharStar);

	}

	/**
	 * Prints a line showing the exit of a function/section and prints the time
	 * taken to finish it. Start time should be passed having the time the function
	 * was started. The function/section will be printed in dashes
	 * 
	 * @param str       function/section name to print
	 * @param startTime start time of the function
	 */
	public static void debugExitFunction(String str, Long startTime) {

		debugExitFunction(Level.INFO, str, startTime);
	}

	/**
	 * Prints a line showing the exit of a function/section and prints the time
	 * taken to finish it. Start time should be passed having the time the function
	 * was started. The function/section will be printed in dashes
	 * 
	 * @param debugLevel debug level to use
	 * @param str        function/section name to print
	 * @param startTime  start time of the function
	 */
	public static void debugExitFunction(Level debugLevel, String str, Long startTime) {

		String outLine = "Leaving " + str + "[" + (System.currentTimeMillis() - startTime) + "] ms";
		debugSpecial(debugLevel, outLine, specialCharDash);

	}

	/**
	 * Prints a line showing the exit of a service and prints the time taken to
	 * finish it. Start time should be passed having the time the function was
	 * started. The service name will be printed in stars. If the result is
	 * specified, it will be also printed
	 * 
	 * @param str       service name to print in the logs
	 * @param result    result to print in the logs
	 * @param startTime time the service started used to calculated the consumed
	 *                  time
	 */
	public static void debugExitService(String str, Result result, Long startTime) {

		debugExitService(Level.INFO, str, result, startTime);
	}

	/**
	 * Prints a line showing the exit of a service and prints the time taken to
	 * finish it. Start time should be passed having the time the function was
	 * started. The service name will be printed in stars. If the result is
	 * specified, it will be also printed
	 * 
	 * @param debugLevel debug level to use
	 * @param str        service name to print in the logs
	 * @param result     result to print in the logs
	 * @param startTime  time the service started used to calculated the consumed
	 *                   time
	 */
	public static void debugExitService(Level debugLevel, String str, Result result, Long startTime) {

		String outLine = "Leaving " + str + "[" + (System.currentTimeMillis() - startTime) + "] ms";
		debugSpecial(debugLevel, outLine, specialCharStar);
		debugResult(debugLevel, result);

	}

	/**
	 * Prints a line showing the exit of a function/section and prints the time
	 * taken to finish it. Start time should be passed having the time the function
	 * was started. The name will be printed in dashes. The result will also be
	 * displayed in the logs
	 * 
	 * @param str       function/section name to show in the logs
	 * @param result    result to print in the logs
	 * @param startTime time the function/section started
	 */
	public static void debugExitFunction(String str, Result result, Long startTime) {

		debugExitFunction(Level.INFO, str, result, startTime);

	}

	/**
	 * Prints a line showing the exit of a function/section and prints the time
	 * taken to finish it. Start time should be passed having the time the function
	 * was started. The name will be printed in dashes. The result will also be
	 * displayed in the logs
	 * 
	 * @param debugLevel debug level to use
	 * @param str        function/section name to show in the logs
	 * @param result     result to print in the logs
	 * @param startTime  time the function/section started
	 */
	public static void debugExitFunction(Level debugLevel, String str, Result result, Long startTime) {

		String outLine = "Leaving " + str + "[" + (System.currentTimeMillis() - startTime) + "] ms";
		debugSpecial(debugLevel, outLine, specialCharDash);
		debugResult(debugLevel, result);

	}

	/**
	 * Prints a line showing the details of a result
	 * 
	 * @param result result object to show the details for
	 */
	public static void debugResult(Result result) {

		debugResult(Level.INFO, result);

	}

	/**
	 * Prints a line showing the details of a result
	 * 
	 * @param debugLevel debug level to use
	 * @param result     result object to show the details for
	 */
	public static void debugResult(Level debugLevel, Result result) {

		String res = "RESULT: "
				+ (result.isSuccess() ? "SUCCESS" : "ERROR: [" + result.getCode() + "] [" + result.getMessage() + "]");
		debugSpecial(debugLevel, res, specialCharStar);

	}

	/**
	 * Generates a debug line special with the specified character
	 * 
	 * @param str       string to set in the line
	 * @param character character to use
	 * @return generated line
	 */
	public static String generateDebugSpecial(String str, String character) {
		return generateDebugLine(0, str, character, maxLineSize);
	}

	/**
	 * Generates a separator line (dashes).
	 * 
	 * @return dashes string with a size 80
	 */
	public static String generateSeparator() {
		return generateSeparator(specialCharDash);
	}

	/**
	 * Generates a separator line (dashes).
	 * 
	 * @return dashes string with a size 80
	 */
	public static String generateSeparator(String character) {
		return generateDebugLine(0, "", character, maxLineSize);
	}

	/**
	 * Generates a debug line using the details passed to it. The string in the
	 * middle surrounded by the special character passed to it. The debug line will
	 * have startingSpaces at the beginning. The resulting line will have as length
	 * the debugLineSize specified
	 * 
	 * @param startingSpaces number of spaces to start the line with
	 * @param str            string to print in the debug line
	 * @param character      character used for the highlighting
	 * @param debugLineSize  total line size
	 * @return formatted output string
	 */
	private static String generateDebugLine(int startingSpaces, String str, String character, int debugLineSize) {
		int len = 0;

		if (StringUtils.isNotBlank(str)) {
			len = str.length();
		}

		int specialCharLength = debugLineSize - len;

		String debugLine = generateSpace(startingSpaces);

		if (len > 0) {

			for (int i = 0; i < specialCharLength / 2; i++)
				debugLine += character;

			debugLine += " " + str + " ";

			for (int i = 0; i < specialCharLength / 2; i++)
				debugLine += character;
		} else {
			debugLine = StringUtils.rightPad(debugLine, debugLineSize + startingSpaces, character);
		}

		return debugLine;
	}

	/**
	 * Prints a tag with a value aligned. Multiple values can be passed
	 * 
	 * @param tag   tag to print
	 * @param value value for the tag
	 */
	public static void tv(Object tag, Object... value) {

		if (!log.isInfoEnabled())
			return;

		tv(Level.INFO, tag, value);
	}

	/**
	 * Prints a tag with a value aligned. Multiple values can be passed
	 * 
	 * @param debugLevel debug level to use
	 * @param tag        tag to print
	 * @param value      one or multiple values for the specified tag
	 */
	public static void tv(Level debugLevel, Object tag, Object... value) {

		printDebugWithLevel(debugLevel, generateTv(tag, false, value));

	}

	/**
	 * Prints a tag with a value aligned. Multiple values can be passed
	 * 
	 * @param debugLevel    debug level to use
	 * @param tag           tag to print
	 * @param includeLength specifies if length should be included in the dump
	 * @param value         one or multiple values for the specified tag
	 */
	public static void tvLen(Level debugLevel, boolean includeLength, Object tag, Object... value) {

		printDebugWithLevel(debugLevel, generateTv(tag, includeLength, value));

	}

	/**
	 * Generates a tag value line. Multiple values can be specified
	 * 
	 * @param tag   tag to print
	 * @param value one or multiple values for the specified tag
	 * @return a string formatted with tag/value
	 */
	public static String generateTv(Object tag, Object... value) {

		return generateTv(tag, false, value);
	}

	/**
	 * Generates a tag value line. Multiple values can be specified
	 * 
	 * @param tag   tag to print
	 * @param value one or multiple values for the specified tag
	 * @return a string formatted with tag/value
	 */
	public static String generateTvWithLen(Object tag, Object... value) {

		return generateTv(tag, true, value);
	}

	/**
	 * Generates a tag value line. Multiple values can be specified
	 * 
	 * @param tag           tag to print
	 * @param includeLength specifies if length should be specified before the
	 *                      object value
	 * @param value         one or multiple values for the specified tag
	 * @return a string formatted with tag/value
	 */
	public static String generateTv(Object tag, boolean includeLength, Object... value) {

		String adjustedTag = generateSpace(lineStartSpaceNumber) + tag;

		adjustedTag = StringUtils.rightPad(adjustedTag, tagValueTagSpace - 1);

		if (value != null) {
			for (Object s : value) {

				String lengthTag = "";

				if (includeLength) {
					int length = 0;
					if (s != null)
						length += String.valueOf(s).length();
					lengthTag += StringUtils.leftPad("(" + String.valueOf(length) + ")", 8, ' ') + "   ";
				}

				if (s instanceof SimpleDateFormat && s != null) {
					adjustedTag += (lengthTag + "[" + ((SimpleDateFormat) s).toPattern() + "]");
				} else if (s instanceof Date) {
					adjustedTag += (lengthTag + "[" + DateUtils.formatDateTimeForDisplay((Date) s) + "]");
				} else {
					adjustedTag += (lengthTag + "[" + s + "]");
				}
			}
		}
		return adjustedTag;
	}

	/**
	 * Generates the specified number of spaces as string.
	 * 
	 * @param numSpaces Number of spaces to generate. If it's negative or zero, an
	 *                  empty string is returned
	 * @return the specified number of spaces as a string
	 */
	public static String generateSpace(int numSpaces) {

		if (numSpaces <= 0)
			return "";

		return StringUtils.leftPad(" ", numSpaces, ' ');

	}

	/**
	 * Dumps the details of the object passed to it. Reflection is used to loop
	 * through each field of the object and prints the value
	 * 
	 * @param o object to dump the details for. If null, nothing is printed
	 */
	public static final void dumpObject(Object o) {
		dumpObject(o, 0);
	}

	/**
	 * Dumps the details of the object passed to it. Reflection is used to loop
	 * through each field of the object and prints the value
	 * 
	 * @param debugLevel debug level to use
	 * @param o          object to dump the details for. If null, nothing is printed
	 */
	public static final void dumpObject(Level debugLevel, Object o) {
		dumpObject(debugLevel, o, 0);
	}

	/**
	 * Dumps the object passed to it with the specified startSpaces at the start of
	 * each line. Reflection is used to loop through each field of the object and
	 * prints the value
	 * 
	 * @param o           object to dump the details for. If null nothing is printed
	 * @param startSpaces number of spaces to ident before each value
	 */
	private static final void dumpObject(Object o, int startSpaces) {

		// No need to do all the processing when the info level is not set
		if (!log.isInfoEnabled())
			return;

		dumpObject(Level.INFO, o, startSpaces);
	}

	/**
	 * Dumps the object passed to it with the specified startSpaces at the start of
	 * each line. Reflection is used to loop through each field of the object and
	 * prints the value
	 * 
	 * @param debugLevel  debug level to use
	 * @param o           object to dump the details for. If null nothing is printed
	 * @param startSpaces number of spaces to ident before each value
	 */
	private static final void dumpObject(Level debugLevel, Object o, int startSpaces) {

		if (o == null)
			return;

		debugSpecial(debugLevel, startSpaces, o.getClass().getSimpleName(), specialCharEquals,
				maxLineSize - startSpaces);

		// If it's a simple type just dump the value
		if (simpleTypes.get(o.getClass().getName()) != null) {
			tvLen(debugLevel, true, "Value", o);
			return;
		}

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = o.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {

			try {
				field.setAccessible(true);
				Object value = field.get(o);
				if ((field.getType().getName().equals(List.class.getName())
						|| field.getType().getName().equals(ArrayList.class.getName())) && value != null) {
					DebugUtils.dumpList(debugLevel, field.getName(), (List<?>) value);
				} else if ((field.getType().getName().equals(Map.class.getName())
						|| field.getType().getName().equals(HashMap.class.getName())) && value != null) {
					DebugUtils.dumpMap(debugLevel, field.getName(), (Map<?, ?>) value);
				} else if (field.getType().getName().equals(JAXBElement.class.getName())) {
					tvLen(debugLevel, true, field.getName(),
							(value == null) ? "" : ((JAXBElement<?>) value).getValue());
				} else if (field.getType().getName().equals(FormatLineParseResult.class.getName()) && value != null) {
					((FormatLineParseResult) value).dumpResult();
				} else {
					tvLen(debugLevel, true, field.getName(), field.get(o));
				}
			} catch (IllegalAccessException ex) {

			}
		}

		debugSpecial(debugLevel, startSpaces, null, specialCharEquals, maxLineSize - startSpaces);
	}

	/**
	 * Dumps a map
	 * 
	 * @param description description of the map to dump
	 * @param map         map to dump
	 */
	public static final void dumpMap(String description, Map<?, ?> map) {
		// Skip processing if the info is not enabled
		if (!log.isInfoEnabled())
			return;

		dumpMap(Level.INFO, description, map);
	}

	/**
	 * Dumps a hash map
	 * 
	 * @param debugLevel  debug level to use
	 * @param description Description of the map being dumped
	 * @param map         map to dump. The values are displayed in tag/value format.
	 *                    If empty, nothing is printed
	 */
	public static final void dumpMap(Level debugLevel, String description, Map<?, ?> map) {

		if (Utils.emptyMap(map))
			return;

		DebugUtils.debugSpecial(debugLevel, description, "-");

		for (Map.Entry<?, ?> entry : map.entrySet()) {
			tvLen(debugLevel, true, entry.getKey(), entry.getValue());
		}

		DebugUtils.debugSeparator(debugLevel, "-");
	}

	/**
	 * Dumps each element of a list
	 * 
	 * @param description description of the list being dumped
	 * @param list        list to dump
	 */
	public static final void dumpList(String description, List<?> list) {
		// Do not do any processing if info is not enabled
		if (!log.isInfoEnabled())
			return;

		dumpList(Level.INFO, description, list);

	}

	/**
	 * Dumps each element of a list
	 * 
	 * @param debugLevel  debug level to use
	 * @param description description of the list being dumped
	 * @param list        list to dump. If empty, nothing is printed
	 */
	public static final void dumpList(Level debugLevel, String description, List<?> list) {

		if (Utils.emptyList(list))
			return;

		DebugUtils.debugSpecial(debugLevel, description, "-");

		for (Object o : list) {
			DebugUtils.dumpObject(debugLevel, o, lineStartSpaceNumber);
		}

		DebugUtils.debugSeparator(debugLevel, "-");
	}

	/**
	 * Prints a dash separator in the debugs
	 */
	public static void debugSeparator() {
		debugSpecial(null, specialCharDash);
	}

	/**
	 * Prints a dash separator in the debugs
	 */
	public static void debugSeparator(Level debugLevel) {
		debugSpecial(debugLevel, null, specialCharDash);
	}

	/**
	 * Prints a separator in the debugs using the character specified
	 */
	public static void debugSeparator(String character) {
		debugSpecial(null, character);
	}

	/**
	 * Prints a separator in the debugs using the character specified
	 * 
	 * @param debugLevel debug level to use
	 * @param character  character to use
	 */
	public static void debugSeparator(Level debugLevel, String character) {
		debugSpecial(debugLevel, null, character);
	}

	/**
	 * Prints a debug with the string in the middle surrounded by the special
	 * character passed to it
	 * 
	 * 
	 * @param str       string to print
	 * @param character character to use for the highlighting
	 */
	public static void debugSpecial(String str, String character) {

		debugSpecial(Level.INFO, 0, str, character, maxLineSize);
	}

	/**
	 * Prints a debug with the string in the middle surrounded by the special
	 * character passed to it
	 * 
	 * @param debugLevel debug level to use
	 * @param str        string to print
	 * @param character  character to use for the highlighting
	 */
	public static void debugSpecial(Level debugLevel, String str, String character) {

		debugSpecial(debugLevel, 0, str, character, maxLineSize);
	}

	/**
	 * Generates a String value in a table format with the specified number of
	 * columns
	 * 
	 * @param columns number of columns the table will have
	 * @param data    the data to print in the table
	 * @return string formatted in a table format
	 */
	public static String renderTable(int columns, String[] data) {

		Table h = new Table(columns);

		for (String field : data) {
			h.addCell(field);
		}

		return (h.render());
	}

	/**
	 * Dumps a byte message with the corresponding hex value
	 * 
	 * @param header  header to use for the dump
	 * @param message actual byte message to dump
	 */
	public static void dump(String header, byte[] message) {

		dump(Level.DEBUG, header, message);

	}

	/**
	 * Dumps a byte message with the corresponding hex value
	 * 
	 * @param debugLevel debug level to use
	 * @param header     header to use for the dump
	 * @param message    actual byte message to dump
	 */
	public static void dump(Level debugLevel, String header, byte[] message) {

		header = "\r\nHex dump for [" + header + "]\r\n";
		int length = header.length();

		header = "\r\n" + stars(length) + header;
		header = header + "\r\n" + HexDump.format(message) + "\r\n" + stars(length);

		printDebugWithLevel(debugLevel, header);

	}

	/**
	 * Emphasizes the specified debug value.
	 * 
	 * @param debug the value to print in the logs. If null, nothing is printed
	 */
	public static void emphasize(String debug) {
		emphasize(Level.DEBUG, debug);
	}

	/**
	 * Emphasizes the specified debug value.
	 * 
	 * @param debugLevel debug level to use
	 * @param debug      the value to print in the logs. If null, nothing is printed
	 */
	public static void emphasize(Level debugLevel, String debug) {
		if (debug == null) {
			return;
		}
		printDebugWithLevel(debugLevel, stars(debug.length()));
		printDebugWithLevel(debugLevel, debug);
		printDebugWithLevel(debugLevel, stars(debug.length()));
	}

	/**
	 * Emphasizes a specific string
	 * 
	 * @param attribute attribute to use as a tag
	 * @param debug     value to be printed for the attribute
	 * @return output string formatted as attribute: debug with a newline after it.
	 *         If debug is null, an empty string is returned
	 */
	public static String emphasizedString(String attribute, String debug) {

		String log = new String("");

		if (debug == null) {
			return StringUtils.EMPTY;
		}
		log += attribute + ":" + debug + "\r\n";
		log += dash(log.length()) + "\r\n";
		return log + "\r\n";
	}

	/**
	 * Generates a line of stars having the specified length
	 * 
	 * @param length length of the output string
	 * @return string with all stars of the specified length
	 */
	public static String stars(int length) {
		return StringUtils.leftPad("*", length, "*");
	}

	/**
	 * Generates a line of dashes having the specified length
	 * 
	 * @param length length of the output string
	 * @return string with all dashes of the specified length
	 */
	public static String dash(int length) {
		return StringUtils.leftPad("-", length, "-");
	}

	/**
	 * Prints a debug with the string in the middle surrounded by the special
	 * character passed to it. The debug line will have startingSpaces at the
	 * beginning. The resulting line will have as length the debugLineSize specified
	 * 
	 * @param debugLevel     debug level to use
	 * @param startingSpaces number of spaces to start the line with
	 * @param str            string to print in the debug line
	 * @param character      character used for the highlighting
	 * @param debugLineSize  total line size
	 */
	private static void debugSpecial(Level debugLevel, int startingSpaces, String str, String character,
			int debugLineSize) {

		printDebugWithLevel(debugLevel, generateDebugLine(startingSpaces, str, character, debugLineSize));
	}

	/**
	 * Prints the debug line specified with the specified debug level
	 * 
	 * @param debugLevel debug level to use
	 * @param line       line to print
	 */
	public static void printDebugWithLevel(Level debugLevel, String line) {

		if (debugLevel == Level.ALL)
			log.fatal(line);
		else if (debugLevel == Level.DEBUG)
			log.debug(line);
		else if (debugLevel == Level.ERROR)
			log.error(line);
		else if (debugLevel == Level.FATAL)
			log.fatal(line);
		else if (debugLevel == Level.INFO)
			log.info(line);
		else if (debugLevel == Level.TRACE)
			log.trace(line);
		else if (debugLevel == Level.WARN)
			log.warn(line);

	}

	/**
	 * Formats an amount for debug
	 * 
	 * @param amount amount to format
	 * @return formatted amount
	 */
	public static String debugAmount(BigDecimal amount) {
		if (amount == null)
			return "NULL";
		return AmountFormatUtils.formatAmountDecimalPlaces(amount, 5);
	}

	/**
	 * Formats an amount for debug
	 * 
	 * @param amount amount to format
	 * @return formatted amount
	 */
	public static String debugAmount(BigDecimal amount, int decimalPlace) {
		if (amount == null)
			return "NULL";
		return AmountFormatUtils.formatAmountDecimalPlaces(amount, decimalPlace);
	}

	public static String substituteMessage(String message, Object... variableInput) {

		List<String> messageArgs = new ArrayList<String>();

		if (variableInput != null && variableInput.length > 0) {
			for (Object object : variableInput) {

				String susbstituionValue = object == null ? "" : String.valueOf(object);
				if (object instanceof Date && object != null)
					susbstituionValue = DateUtils.formatDateForDisplay((Date) object);
				else if (object instanceof Timestamp && object != null)
					susbstituionValue = DateUtils.formatDateTimeForDisplay((Timestamp) object);

				messageArgs.add(susbstituionValue);
			}
		}

		return substituteMessage(message, messageArgs);

	}

	private static String substituteMessage(String message, List<String> variableInput) {

		if (variableInput != null && variableInput.size() > 0) {
			for (Object object : variableInput) {

				Object susbstituionValue = object;
				if (object instanceof Date && object != null)
					susbstituionValue = DateUtils.formatDateForDisplay((Date) object);
				else if (object instanceof Timestamp && object != null)
					susbstituionValue = DateUtils.formatDateTimeForDisplay((Timestamp) object);

				message = message.replaceFirst("\\[var\\]",
						"[" + Matcher.quoteReplacement(String.valueOf(susbstituionValue)) + "]");
			}
		}

		return message;
	}
}
