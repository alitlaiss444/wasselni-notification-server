package com.wasselni.common.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

public class Utils {

	private static final Log log = LogFactory.getLog(Utils.class);

	/**
	 * Trims the string passed to it.
	 * 
	 * @return the trimmed value if the data is not null, an empty string otherwise
	 */
	public static String trimSpecial(String data) {
		if (data == null) {
			return "";
		} else {
			return data.trim();
		}
	}

	/**
	 * Parses a big decimal string
	 * 
	 * @param value        string value to be parsed to a big decimal
	 * @param defaultValue default value to return in case parsing failed
	 * @return a big decimal with the value specified in case valid, the default
	 *         value otherwise
	 */
	public static BigDecimal parseBigDecimal(String value, BigDecimal defaultValue) {

		if (null == value)
			return defaultValue;
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * parse an string to an integer, if parsing failure, returns the defaultValue
	 * 
	 * @param value        value to parse
	 * @param defaultValue default value to return in case of failure
	 * @return an integer with the value specified in case valid, the default value
	 *         otherwise
	 */
	public static int parseInteger(String value, int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * parse an string to a long, if parsing failed, returns the defaultValue
	 * 
	 * @param value        value to parse
	 * @param defaultValue default value to return in case of failure
	 * @return a long with the value specified in case valid, the default value
	 *         otherwise
	 */
	public static long parseLong(String value, long defaultValue) {
		try {
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * parse an string to an short, if parsing failure, returns the defaultValue
	 * 
	 * @param value        value to parse
	 * @param defaultValue default value to return in case of failure
	 * @return a short with the value specified in case valid, the default value
	 *         otherwise
	 */
	public static short parseShort(String value, short defaultValue) {
		try {
			return Short.valueOf(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Checks if a list is null or empty
	 * 
	 * @param list list to check
	 * @return true if the list is null or empty, false otherwise
	 */
	public static boolean emptyList(List<?> list) {

		if (list == null || list.isEmpty())
			return true;

		return false;
	}

	/**
	 * Checks if a map is null or empty
	 * 
	 * @param map map to check if null or empty
	 * @return true if the map is null or empty, false otherwise
	 */
	public static boolean emptyMap(Map<?, ?> map) {

		if (map == null || map.isEmpty())
			return true;

		return false;

	}

	/**
	 * Checks if a value is in an list
	 * 
	 * @param description description of the list to show in the logs
	 * @param list        list to check for the value in
	 * @param value       value to check if present in the list
	 * @return true if the value is in the list. false if the list is empty, value
	 *         is null or the value does not exist in the list
	 */
	public static boolean isInList(String description, List<?> list, Object value) {

		return isInList(description, list, value, true);
	}

	/**
	 * Checks if a value is in an list without showing any log
	 * 
	 * @param list  list to check for the value in
	 * @param value value to check in the list
	 * @return true if the value is in the list. false if the list is empty, value
	 *         is null or the value does not exist in the list
	 */
	public static boolean isInListNoDbg(List<?> list, Object value) {

		return isInList(null, list, value, false);
	}

	/**
	 * Checks if a value is in an list
	 * 
	 * @param description description of the list to show in the logs
	 * @param list        list to check for the value in
	 * @param value       value to check in the list
	 * @param debug       specifies if logs should be displayed with the details of
	 *                    the check
	 * @return true if the value is in the list. false if the list is empty, value
	 *         is null or the value does not exist in the list
	 */
	private static boolean isInList(String description, List<?> list, Object value, boolean debug) {

		boolean found = false;

		if (debug)
			log.debug(description + ": Verifying if [" + value + "] is in list [" + list + "]");

		/* If the list provided is null or empty, assume not found */
		if (emptyList(list)) {
			if (debug)
				log.debug("List provided is empty, assuming not found");
		}
		/* If value provided is null or empty, assume not found */
		else if (value == null || StringUtils.isBlank(String.valueOf(value))) {
			if (debug)
				log.debug("Value provided null or empty, assuming not found");
		}
		/* Both value and list are provided do actual checking */
		else {
			for (Object o : list) {
				if (String.valueOf(o).equals(String.valueOf(value)))
					found = true;
			}
		}

		if (debug)
			log.debug(description + ": value [" + (found ? "exists" : "does not exists") + "] in list");

		return found;

	}

	/**
	 * Does verification on the string passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing)
	 * 
	 * @param value           value to check
	 * @param min             minimum length that the value should have. if passed
	 *                        as null, the value is considered as having no minimum
	 * @param max             maximum length that the value can have. If passed as
	 *                        null, the value is considered as having no maximum
	 * @param mandatory       specifies if the value is mandatory (cannot be empty)
	 * @param descr           description of the value being checked, used for
	 *                        debugs and exception throwing
	 * @param defaultValue    default value to use in case the value is empty and
	 *                        not mandatory
	 * @param supportedValues list of supported values that the value can have. If
	 *                        passed as null or empty, no checks are done
	 * @throws SystemException
	 */
	public static String verifyMinMaxMandatory(String value, Integer min, Integer max, boolean mandatory, String descr,
			String defaultValue, List<?> supportedValues) throws SystemException {

		value = verifyMinMaxMandatory(value, min, max, mandatory, descr, defaultValue);

		if (StringUtils.isNotBlank(value) && !Utils.emptyList(supportedValues)) {

			if (!Utils.isInListNoDbg(supportedValues, value)) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE_SUPPORTED, value, descr, supportedValues);
			}
		}

		return value;
	}

	/**
	 * Verifies a string date and returns it parsed
	 * 
	 * @param date         date string to parse and verify
	 * @param mandatory    boolean specifying if the value is mandatory or not
	 * @param descr        description of the date field used in debugs and
	 *                     exception throwing
	 * @param defaultValue default date value to be used if the string passed is
	 *                     empty and the value is not mandatory
	 * @param sdf          simple date format to be used to parse the date. An
	 *                     unknown system exception is thrown in case passed as null
	 * @return the parsed date value in case specified, the default value otherwise
	 * @throws SystemException
	 */
	public static Date verifyDate(String date, boolean mandatory, String descr, Date defaultValue, SimpleDateFormat sdf)
			throws SystemException {

		if (StringUtils.isBlank(date)) {
			if (mandatory) {
				throw new SystemException(SystemError.VE_MISSING_MANDATORY, descr);
			} else
				return defaultValue;
		}

		if (sdf == null)
			throw new SystemException(SystemError.UNKNOWN_ERROR);

		if (sdf.toPattern().length() != date.length()) {
			throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, date, descr);
		}

		try {
			// Make the parsing strict - the data should be exactly matching the
			// format
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (Exception e) {
			throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, date, descr);
		}

	}

	/**
	 * Does verification on the string passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing) Also type is
	 * supported, so the object returned is actually casted to the required type
	 * 
	 * @param value        value to verify
	 * @param type         type of the field. The value is verified that it can be
	 *                     cast to the specified type
	 * @param min          minimum length that the value should have. if passed as
	 *                     null, the value is considered as having no minimum
	 * @param max          maximum length that the value can have. If passed as
	 *                     null, the value is considered as having no maximum
	 * @param mandatory    specifies if the value is mandatory (cannot be empty)
	 * @param descr        description of the value being checked, used for debugs
	 *                     and exception throwing
	 * @param defaultValue default value to use in case the value is empty and not
	 *                     mandatory
	 * @throws SystemException
	 */
	public static Object verifyMinMaxMandatory(String value, Class<?> type, Integer min, Integer max, boolean mandatory,
			String descr, String defaultValue) throws SystemException {

		value = verifyMinMaxMandatory(value, min, max, mandatory, descr, defaultValue);

		Object out = value;

		if (StringUtils.isNotBlank(value))
			out = validateDataType(value, descr, type);

		return out;
	}

	/**
	 * Does verification on the string passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing) Also type is
	 * supported, so the object returned is actually casted to the required type. A
	 * list of supported values can also be specified and the value is verified that
	 * it actually is in the list
	 * 
	 * @param value           value to verify
	 * @param type            type of the field. The value is verified that it can
	 *                        be cast to the specified type
	 * @param min             minimum length that the value should have. if passed
	 *                        as null, the value is considered as having no minimum
	 * @param max             maximum length that the value can have. If passed as
	 *                        null, the value is considered as having no maximum
	 * @param mandatory       specifies if the value is mandatory (cannot be empty)
	 * @param descr           description of the value being checked, used for
	 *                        debugs and exception throwing
	 * @param defaultValue    default value to use in case the value is empty and
	 *                        not mandatory
	 * @param supportedValues list of supported values. in case not specified no
	 *                        checks are done
	 * @throws SystemException
	 */
	public static Object verifyMinMaxMandatory(String value, Class<?> type, Integer min, Integer max, boolean mandatory,
			String descr, String defaultValue, List<?> supportedValues) throws SystemException {

		value = verifyMinMaxMandatory(value, min, max, mandatory, descr, defaultValue);

		Object out = value;

		if (StringUtils.isNotBlank(value))
			out = validateDataType(value, descr, type);

		if (StringUtils.isNotBlank(value) && !Utils.emptyList(supportedValues)) {
			if (!Utils.isInListNoDbg(supportedValues, value)) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE_SUPPORTED, value, descr, supportedValues);
			}
		}

		return out;
	}

	/**
	 * Validates the data type
	 * 
	 * @param value string value to validate
	 * @param descr description of the value for debugging and exception throwing
	 * @param type  type to validate
	 * @throws SystemException
	 */
	public static Object validateDataType(String value, String descr, Class<?> type) throws SystemException {

		if (type == BigDecimal.class) {
			try {
				return new BigDecimal(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Float.class) {
			try {
				return Float.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Long.class) {
			try {
				return Long.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Integer.class) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Short.class) {
			try {
				return Short.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Boolean.class) {
			try {
				return Boolean.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (type == Double.class) {
			try {
				return Double.valueOf(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		if (value == null)
			return null;
		else
			return String.valueOf(value);
	}

	/**
	 * Does verification on the string passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing)
	 * 
	 * @param value        value to verify
	 * @param min          minimum length that the value should have. if passed as
	 *                     null, the value is considered as having no minimum
	 * @param max          maximum length that the value can have. If passed as
	 *                     null, the value is considered as having no maximum
	 * @param mandatory    specifies if the value is mandatory (cannot be empty)
	 * @param descr        description of the value being checked, used for debugs
	 *                     and exception throwing
	 * @param defaultValue default value to use in case the value is empty and not
	 *                     mandatory
	 * @throws SystemException
	 */
	public static String verifyMinMaxMandatory(String value, Integer min, Integer max, boolean mandatory, String descr,
			String defaultValue) throws SystemException {

		if (StringUtils.isBlank(value)) {
			if (mandatory)
				throw new SystemException(SystemError.VE_MISSING_MANDATORY, descr);
			else
				return defaultValue;
		} else {
			if (min != null) {
				if (value.length() < min) {
					throw new SystemException(SystemError.VE_LENGTH_LESS_THAN_MINIMUM, descr, value, value.length(),
							min);
				}
			}
			if (max != null) {
				if (value.length() > max) {
					throw new SystemException(SystemError.VE_LENGTH_GREATER_THAN_MAXIMUM, descr, value, value.length(),
							max);
				}
			}
		}

		return value;
	}

	/**
	 * Does verification on the short passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing)
	 * 
	 * @param value           short value to check
	 * @param min             minimum value that the short field can have. If set to
	 *                        null the value is considered as having no minimum
	 * @param max             maximum value that the short field can have. If set to
	 *                        null the value is considered as having no maximum
	 * @param mandatory       boolean specifying if the field is mandatory or not
	 *                        (can be null)
	 * @param descr           description of the field used for debugging and
	 *                        exception throwing
	 * @param defaultValue    default value to use in case the field is not present
	 *                        and it's not mandatory
	 * @param supportedValues list of supported values for this field. If not
	 *                        specified, no checks are done on it
	 * @throws SystemException
	 */
	public static Short verifyMinMaxMandatory(Short value, Short min, Short max, boolean mandatory, String descr,
			Short defaultValue, List<?> supportedValues) throws SystemException {

		value = verifyMinMaxMandatory(value, min, max, mandatory, descr, defaultValue);

		if (null != value && !Utils.emptyList(supportedValues)) {
			if (!Utils.isInListNoDbg(supportedValues, value)) {
				throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, descr);
			}
		}

		return value;
	}

	/**
	 * Does verification on the short passed to it (if it's less than the minimum
	 * length or exceeds the maximum length, mandatory but missing)
	 * 
	 * @param value        short value to check
	 * @param min          minimum value that the short field can have. If set to
	 *                     null the value is considered as having no minimum
	 * @param max          maximum value that the short field can have. If set to
	 *                     null the value is considered as having no maximum
	 * @param mandatory    boolean specifying if the field is mandatory or not (can
	 *                     be null)
	 * @param descr        description of the field used for debugging and exception
	 *                     throwing
	 * @param defaultValue default value to use in case the field is not present and
	 *                     it's not mandatory
	 * @throws SystemException
	 */
	public static Short verifyMinMaxMandatory(Short value, Short min, Short max, boolean mandatory, String descr,
			Short defaultValue) throws SystemException {

		if (null == value) {
			if (mandatory)
				throw new SystemException(SystemError.VE_MISSING_MANDATORY, descr);
			else
				return defaultValue;
		} else {
			if (min != null) {
				if (value < min) {
					throw new SystemException(SystemError.VE_VALUE_LESS_THAN_MINIMUM, descr, value, min);
				}
			}
			if (max != null) {
				if (value > max) {
					throw new SystemException(SystemError.VE_VALUE_GREATER_THAN_MAXIMUM, descr, value, max);
				}
			}
		}

		return value;
	}

	/**
	 * Checks if a specified value exists in a comma separated list
	 * 
	 * @param description description of the comma separated list being checked
	 * @param list        list to check for the value in
	 * @param value       value to be checked in the list
	 * @return true if the value exists in the list, false if the list is empty, the
	 *         value is empty or actually does not exist in the list
	 */
	public static boolean isInCommaSeparatedList(String description, String list, String value) {

		return isInCommaSeparatedList(description, list, value, true);
	}

	/**
	 * Checks if a specified value exists in a comma separated list without showing
	 * debugs
	 * 
	 * @param list  list to check for the value in
	 * @param value value to be checked in the list
	 * @return true if the value exists in the list, false if the list is empty, the
	 *         value is empty or actually does not exist in the list
	 */
	public static boolean isInCommaSeparatedListNoDbg(String list, String value) {

		return isInCommaSeparatedList(null, list, value, false);
	}

	/**
	 * Checks if a specified value exists in a comma separated list
	 * 
	 * @param description description of the comma separated list being checked
	 * @param list        list to check for the value in
	 * @param value       value to be checked in the list
	 * @param debug       specifies if debugs should be shown about the details of
	 *                    the check
	 * @return true if the value exists in the list, false if the list is empty, the
	 *         value is empty or actually does not exist in the list
	 */
	public static boolean isInCommaSeparatedList(String description, String list, String value, boolean debug) {

		boolean ret = false;

		if (debug)
			log.debug(trimSpecial(description) + ": verifying if [" + value + "] exists in the list [" + list + "]");

		// if the list is not provided, return false
		if (StringUtils.isBlank(list)) {

			if (debug)
				log.debug(trimSpecial(description) + ": List provided is empty, assuming value not in list");
			ret = false;
		}
		// if the value is not provided, return false
		else if (StringUtils.isBlank(value)) {
			if (debug)
				log.debug(trimSpecial(description) + ": Value provided is empty, assuming not in list");
			return false;
		}
		// both value and list provided
		else {
			String parts[] = list.split(",");
			ret = false;
			for (String part : parts) {
				if (part.equals(value))
					ret = true;
			}
		}

		if (debug)
			log.debug(trimSpecial(description) + ": Value [" + (ret ? "exists" : "does not exist") + "] in list");

		return ret;
	}

	/**
	 * Gets a map value from a simple mapping having each map pair separated by a :
	 * and the pairs separated by comma. Debugs are not displayed by this function
	 * 
	 * @param description description of the map, used for debugging
	 * @param map         map to get the mapping from
	 * @param fromValue   the from value to get the map for
	 * @return the mapped value if found, null in case the map is empty, the from
	 *         value is empty or no map found
	 */
	public static String getMapValueNoDbg(String map, String fromValue) {
		return getMapValue(null, map, fromValue, false);
	}

	/**
	 * Gets a map value from a simple mapping having each map pair separated by a :
	 * and the pairs separated by comma. Debugs are displayed by this function
	 * 
	 * @param description description of the map, used for debugging
	 * @param map         map to get the mapping from
	 * @param fromValue   the from value to get the map for
	 * @return the mapped value if found, null in case the map is empty, the from
	 *         value is empty or no map found
	 */
	public static String getMapValue(String description, String map, String fromValue) {
		return getMapValue(description, map, fromValue, true);
	}

	/**
	 * Convert a string map in the format V1:V2,V3:V4 to a hashmap
	 * 
	 * @param description description of the map for debugs
	 * @param stringMap   string map to convert
	 * @return converted map
	 */
	public static Map<String, String> convertMap(String description, String stringMap) {

		if (StringUtils.isBlank(stringMap))
			return null;

		Map<String, String> map = new HashMap<String, String>();

		String[] maps = stringMap.split(",");

		for (String individualMap : maps) {

			String[] internalMap = individualMap.split(":");
			if (internalMap.length != 2) {
				log.warn("Invalid map [" + individualMap + "] for [" + description + "] skipping");
			} else {
				map.put(internalMap[0], internalMap[1]);
			}
		}

		return map;
	}

	/**
	 * Gets a map value from a simple mapping having each map pair separated by a :
	 * and the pairs separated by comma
	 * 
	 * @param description description of the map, used for debugging
	 * @param map         map to get the mapping from
	 * @param fromValue   the from value to get the map for
	 * @param debug       specifies if debugs should be displayed about the details
	 * @return the mapped value if found, null in case the map is empty, the from
	 *         value is empty or no map found
	 */
	public static String getMapValue(String description, String map, String fromValue, boolean debug) {

		if (debug)
			log.debug(description + ": Verifying if a map exists for [" + fromValue + "] in [" + map + "]");

		if (StringUtils.isBlank(map)) {
			if (debug)
				log.debug(description + ": map provided empty, assuming no mapping");
			return null;
		}

		if (StringUtils.isBlank(fromValue)) {
			if (debug)
				log.debug(description + ": value provided is empty, assuming no mapping");
			return null;
		}

		String[] parts = map.split(",");

		for (String part : parts) {
			String[] innerPart = part.split(":");
			if (innerPart.length != 2) {
				if (debug)
					log.debug("Invalid part [" + part + "] found in mapping, skipping");
				continue;
			}

			if (innerPart[0].equals(fromValue)) {
				if (debug)
					log.debug(description + ": found mapping value [" + innerPart[1] + "] for [" + fromValue + "]");
				return innerPart[1];
			}
		}

		if (debug)
			log.debug(description + ": No mapping found for [" + fromValue + "]");
		return null;
	}

	/**
	 * Checks if a mapping exists between the first and second value. Map format
	 * will be from1:to1,from2:to2. If the map provided is null, the mapping is
	 * considered as does not exist and false will be returned
	 * 
	 * @param description description of the map being checked used for debugging
	 * @param map         the map to get check for the mapping in
	 * @param fromValue   the value to check the map for
	 * @param toValue     the value to check the map for
	 * @return true if a mapping exists between the from and to values specified,
	 *         false in case the map is empty or either from and to values are empty
	 *         or a map does not exist
	 */
	public static boolean mapExists(String description, String map, String fromValue, String toValue) {

		log.debug(
				description + ": Verifying map from [" + fromValue + "] to [" + toValue + "] exists in [" + map + "]");

		if (StringUtils.isBlank(map)) {
			log.debug(description + ": map provided empty, assuming no match");
			return false;
		}

		if (StringUtils.isBlank(fromValue)) {
			log.debug(description + ": from value provided is empty, assuming no match");
			return false;
		}

		if (StringUtils.isBlank(toValue)) {
			log.debug(description + ": to value provided is empty, assuming no match");
			return false;
		}

		String[] parts = map.split(",");

		for (String part : parts) {
			String[] innerPart = part.split(":");
			if (innerPart.length != 2) {
				log.debug("Invalid part [" + part + "] found in mapping, skipping");
				continue;
			}

			if (innerPart[0].equals(fromValue) && innerPart[1].equals(toValue)) {
				log.debug(description + ": found mapping between [" + fromValue + "] and [" + toValue + "]");
				return true;
			}
		}

		log.debug(description + ": No mapping found between [" + fromValue + "] and [" + toValue + "]");
		return false;
	}

	/**
	 * Escapes all chars that are not alphanumeric
	 * 
	 * @param str string to escape all non-alphanumeric characters in
	 * @return the output string with all non-alphanumeric values escaped
	 */
	public static String escapeAllNonAlphaNum(String str) {
		String out = "";

		for (int i = 0; i < str.length(); i++) {
			if (StringUtils.isAlphanumeric("" + str.charAt(i)))
				out += "" + str.charAt(i);
			else
				out += "\\" + str.charAt(i);
		}

		return out;
	}

	/**
	 * Generates the specified number of question marks for a query
	 * 
	 * @param number number of question marks to generate
	 * @return a comma separated string of question marks to be used for a query
	 */
	public static String generateQuestionMarks(int number) {
		String out = "";

		if (number > 0) {
			for (int i = 0; i < number; i++) {
				if (StringUtils.isNotBlank(out))
					out += " , ";
				out += "?";
			}
		}
		return out;
	}

	/**
	 * Converts a list to an Object array
	 * 
	 * @param list list to convert
	 * @return object array converted from list. null in case the list provided is
	 *         empty
	 */
	public static Object[] listToObjectArray(List<?> list) {

		if (Utils.emptyList(list))
			return null;

		Object[] ret = new Object[list.size()];

		int i = 0;
		for (Object o : list) {
			ret[i] = o;
			i++;
		}

		return ret;

	}

	/**
	 * Gets an environment variable value
	 * 
	 * @param variableName environment variable value to get the value for
	 * @return the value of the environment value, null in case not found
	 */
	public static String getEnvironmentVariables(String variableName) {

		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {

			if (envName.equals(variableName)) {
				if (StringUtils.isBlank(envName))
					return "";
				else
					return env.get(envName);
			}
		}
		return null;
	}

	/**
	 * Converts all sequences of spaces to one space
	 * 
	 * @param in the string to convert the spaces in
	 * @return empty string in case in was null, converted string not having more
	 *         than 1 consecutive space
	 */
	public static String allSpacesToOne(String in) {

		if (null == in)
			return "";

		return in.replaceAll("^ +| +$|( )+", "$1");
	}

	/**
	 * Checks if a string might be Arabic
	 * 
	 * @param s string to check
	 * @return true if the string might contain arabic, false otherwise
	 */
	public static boolean isProbablyArabic(String s) {
		for (int i = 0; i < s.length();) {
			int c = s.codePointAt(i);
			if (c >= 0x0600 && c <= 0x06E0)
				return true;
			i += Character.charCount(c);
		}
		return false;
	}

	/**
	 * Checks if a value matches a wild card expression
	 * 
	 * @param value              value to check
	 * @param wildCardExpression wild card expression to check against
	 * @return true if the value matches the wildcard expression, false otherwise
	 */
	public static boolean matchesWildCard(String value, String wildCardExpression) {
		return value.matches(wildCardExpression.replace("?", ".?").replace("*", ".*?"));
	}

	/**
	 * Checks if a string is a valid base 64 encoded
	 * 
	 * @param s string to check
	 * @return true if the string is a valid base 64 encoded string, false otherwise
	 *         or if empty
	 */
	public static boolean isValidBase64EncodedString(String s) {
		if (StringUtils.isBlank(s))
			return false;

		return s.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
	}

	/**
	 * Removes the end of line from a string
	 * 
	 * @param s string to remove the end of line from
	 * @return string with no end of line, null in case the original string was null
	 */
	public static String removeEndOfLine(String s) {
		if (s == null)
			return s;

		return s.replaceAll("\r\n", "").replace("\r", "").replace("\n", "");
	}

	/**
	 * Removes the leading zeroes from a a string
	 * 
	 * @param s string to remote the leading zeroes from
	 * @return string with removed leading zeroes if any. In case the input is null,
	 *         null is returned
	 */
	public static String removeLeadingZeroes(String s) {

		if (s == null)
			return s;

		return s.replaceFirst("^0+(?!$)", "");
	}

	/**
	 * Converts a unicode string to hex char
	 * 
	 * @param uniCodeString unicode string to convert to hex
	 * @return output hex value. null in case the unicode string specified is null
	 *         or empty
	 */
	public static String unicodeToHexString(String uniCodeString) {

		if (StringUtils.isBlank(uniCodeString))
			return null;

		String hexString = "";

		for (int i = 0; i < uniCodeString.length(); i++) {
			hexString += StringUtils.leftPad(Integer.toHexString(uniCodeString.charAt(i)), 2, '0');
		}

		return hexString;
	}

	/**
	 * Generates a random number as string with the specified length
	 * 
	 * @param len length of the random number to generate
	 * @return random number as string
	 */
	public static String generateRandom(int len) {

		String ran = "";
		Random random = new Random();

		for (int i = 0; i < len; i++) {
			ran += random.nextInt(10);
		}

		return ran;
	}

	/**
	 * Splits a line and includes empty parts (returned as space) for 2 consecutives
	 * separators
	 * 
	 * @param line      line to split
	 * @param separator separator to use. The value is escaped before being used so
	 *                  no need to escape it
	 * @return string array of the split
	 */
	public static String[] splitIncludeEmpty(String line, String separator) {

		if (null == line)
			return null;

		while (line.contains(separator + separator)) {
			line = line.replace(separator + separator, separator + " " + separator);
		}

		return line.split(escapeAllNonAlphaNum(separator));
	}

	/**
	 * Gets a string with only numbers extracted from the specified value.
	 * 
	 * @param value value to extract the numbers from
	 * @return string with only numbers extracted from the original provided value.
	 *         if the original value is null, null is returned. In case it's empty,
	 *         an empty string is returned
	 */
	public static String getOnlyNumbersFromString(String value) {

		if (value == null)
			return value;
		else if (value.isEmpty())
			return "";

		return value.replaceAll("\\D+", "");
	}

	/**
	 * Gets only the alpha characters from a specified string
	 * 
	 * @param value value to get the alpha values from
	 * @return string with only alpha characters from the original string
	 */
	public static String getOnlyAlphaCharactersFromString(String value) {

		if (value == null)
			return value;
		else if (value.isEmpty())
			return value;

		return value.replaceAll("[^A-Za-z]+", "");
	}

	/**
	 * Gets only alpha numeric value from a string
	 * 
	 * @param value value to get the alpha numeric values from
	 * @return alpha numeric value extracted from the original string
	 */
	public static String getOnlyAlphaNumericFromString(String value) {

		if (value == null)
			return value;
		else if (value.isEmpty())
			return value;

		return value.replaceAll("[^A-Za-z0-9 ]", "");
	}

	/**
	 * Trims a string from the left
	 * 
	 * @param value value to trim
	 * @return trimmed value or an empty string in case the value is blank
	 */
	public static String trimLeft(String value) {
		if (StringUtils.isBlank(value))
			return "";
		return value.replaceAll("^\\s+", "");

	}

	/**
	 * Trims a string from the right
	 * 
	 * @param value value to trim
	 * @return trimmed value or an empty string in case the value is blank
	 */
	public static String rightTrim(String value) {

		if (StringUtils.isBlank(value))
			return "";

		return value.replaceAll("\\s+$", "");
	}

	/**
	 * trims a string from the right if the length is greater than the specified one
	 * 
	 * @param value     value to check/trim
	 * @param maxLength maximum length for the value
	 * @return trimmed/original value depending if it fits the max length
	 */
	public static String trimIfGreater(String value, int maxLength) {
		if (StringUtils.isBlank(value))
			return null;

		if (value.length() > maxLength)
			return value.trim().substring(0, maxLength - 1);

		return value.trim();

	}

	/**
	 * Creates a Map with the maximum entries as the ones specifies and in which the
	 * eldest entries are removed once this limit is reached
	 * 
	 * @param maxEntries maximum number of entries for the map
	 * @return {@link Map} with a capacity the maximum number of entries specified
	 *         and which removes the eldest entries once the limit is reached
	 */
	public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
		return new LinkedHashMap<K, V>(maxEntries * 10 / 7, 0.7f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > maxEntries;
			}
		};
	}

	/**
	 * Converts a numeric English value to Arabic. All non-numeric values will
	 * remain in the original format
	 * 
	 * @param in input string to convert
	 * @return converted string
	 */
	public static String numericToArabicNumber(String in) {
		if (StringUtils.isBlank(in))
			return in;

		char[] arabicChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < in.length(); i++) {
			if (Character.isDigit(in.charAt(i))) {
				builder.append(arabicChars[(int) (in.charAt(i)) - 48]);
			} else {
				builder.append(in.charAt(i));
			}
		}
		return builder.toString();
	}

	/**
	 * Make a copy of the list to avoid changing the values of the original list
	 * 
	 * @param list list to copy
	 * @return copy of the list
	 */
	public static <T> List<T> copyList(List<T> list) {

		List<T> newList = new ArrayList<T>();
		newList.addAll(list);

		return newList;
	}

	public static boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * Checks if a string is english characters only
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEnglishString(String str) {
		if (str == null)
			return true;
		return str.matches("\\A\\p{ASCII}*\\z");
	}

	/**
	 * Generates a random alphabetic string with the specified length
	 * 
	 * @param len length of the random number to generate
	 * @return random alpha as string
	 */
	public static String generateRandomAlpha(int len) {

		String ran = "";
		SecureRandom random = new SecureRandom();
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < len; i++) {
			ran += characters.charAt(random.nextInt(characters.length()));
		}

		return ran;
	}

	/**
	 * Generates a random alpha-numeric[A-Z/0-9] string with the specified length
	 * 
	 * @param len length of the random string to generate
	 * @return random alphanumeric as string
	 */
	public static String generateRandomAlphaNumeric(int len) {

		String ran = "";
		SecureRandom random = new SecureRandom();
		String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < len; i++) {
			ran += characters.charAt(random.nextInt(characters.length()));
		}

		return ran;
	}

	/**
	 * Converts a string to upper case and removes all non-alphanumeric characters
	 * from it
	 * 
	 * @param in
	 * @return
	 */
	public static String toUpperNoSpecialChars(String in) {

		String out = in.toUpperCase();

		return out.replaceAll("[^A-Za-z0-9 ]", " ");

	}

	/**
	 * checks if input is a number
	 * 
	 * @param strNum input
	 * @return true in case strNum is a number of decimal
	 */
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
