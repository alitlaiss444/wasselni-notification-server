package com.wasselni.common.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;

/**
 * Utility functions to handle bundles
 *
 */
public class BundleUtils {

	private static final Log log = LogFactory.getLog(BundleUtils.class);

	/**
	 * Gets an Object-Object map from a bundle
	 * 
	 * @param bundleName  name of the bundle to read/parse
	 * @param prefix      prefix of the properties name to read. if not specified
	 *                    all properties are read otherwise only the properties
	 *                    starting with this value are read
	 * @param description description of the bundle being parsed, used for logging
	 *                    purposes
	 * @return a map of the properties read from the bundle. If no entries are found
	 *         or the bundle is not found, an empty map is returned
	 */
	public static Map<Object, Object> getBundle(String bundleName, String prefix, String description) {

		Map<Object, Object> map = new HashMap<Object, Object>();

		if (StringUtils.isBlank(bundleName))
			return map;

		if (StringUtils.isBlank(prefix))
			prefix = "";

		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(bundleName);
		} catch (MissingResourceException e) {
			log.warn("Resource [" + bundleName + ".properties] not found, returning empty map");
			return map;
		}

		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			if (key.startsWith(prefix))
				map.put(key.substring(prefix.length()), value);
		}
		DebugUtils.dumpMap(description, map);

		return map;
	}

	/**
	 * Gets an Integer-Object map from a bundle
	 * 
	 * @param bundleName  bundle name to read and get the properties from
	 * @param prefix      prefix of the properties name to read. if not specified
	 *                    all properties are read otherwise only the properties
	 *                    starting with this value are read
	 * @param description of the bundle being parsed, used for logging purposes
	 * @return a map of the properties read from the bundle. If no entries are found
	 *         or the bundle is not found, an empty map is returned
	 */
	public static Map<Integer, String> getIntStringBundle(String bundleName, String prefix, String description) {

		Map<Integer, String> map = new HashMap<Integer, String>();

		if (StringUtils.isBlank(bundleName))
			return map;

		if (StringUtils.isBlank(prefix))
			prefix = "";

		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(bundleName);
		} catch (MissingResourceException e) {
			log.warn("Resource [" + bundleName + "] not found, returning empty map");
			return map;
		}
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			if (key.startsWith(prefix))
				try {
					map.put(Integer.valueOf(key.substring(prefix.length())), value);
				} catch (Exception e) {
					log.warn("Invalid integer found [" + key.substring(prefix.length()) + "] skipping value");
				}
		}
		DebugUtils.dumpMap(description, map);

		return map;
	}

	/**
	 * Gets an String-Object map from a bundle
	 * 
	 * @param bundleName  bundle name to read and get the properties from
	 * @param prefix      prefix of the properties name to read. if not specified
	 *                    all properties are read otherwise only the properties
	 *                    starting with this value are read
	 * @param description of the bundle being parsed, used for logging purposes
	 * @param toUpperKey  specifies if the keys should be converted to upper case in
	 *                    the generated map
	 * @return a map of the properties read from the bundle. If no entries are found
	 *         or the bundle is not found, an empty map is returned
	 */
	public static Map<String, String> getStringStringBundle(String bundleName, String prefix, String description,
			boolean toUpperKey) {

		return getStringStringBundle(bundleName, prefix, description, toUpperKey, Level.INFO);
	}

	/**
	 * Gets an String-Object map from a bundle
	 * 
	 * @param bundleName  bundle name to read and get the properties from
	 * @param prefix      prefix of the properties name to read. if not specified
	 *                    all properties are read otherwise only the properties
	 *                    starting with this value are read
	 * @param description of the bundle being parsed, used for logging purposes
	 * @param toUpperKey  specifies if the keys should be converted to upper case in
	 *                    the generated map
	 * @param logLevel    logLevel
	 * @return a map of the properties read from the bundle. If no entries are found
	 *         or the bundle is not found, an empty map is returned
	 */
	public static Map<String, String> getStringStringBundle(String bundleName, String prefix, String description,
			boolean toUpperKey, Level logLevel) {
		return getStringStringBundle(bundleName, prefix, description, toUpperKey, logLevel, false);
	}

	/**
	 * Gets an String-Object map from a bundle
	 * 
	 * @param bundleName     bundle name to read and get the properties from
	 * @param prefix         prefix of the properties name to read. if not specified
	 *                       all properties are read otherwise only the properties
	 *                       starting with this value are read
	 * @param description    of the bundle being parsed, used for logging purposes
	 * @param toUpperKey     specifies if the keys should be converted to upper case
	 *                       in the generated map
	 * @param logLevel       logLevel
	 * @param filterByPrefix return map for entries starting with prefix only
	 * @return a map of the properties read from the bundle. If no entries are found
	 *         or the bundle is not found, an empty map is returned
	 */
	public static Map<String, String> getStringStringBundle(String bundleName, String prefix, String description,
			boolean toUpperKey, Level logLevel, boolean filterByPrefix) {

		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isBlank(bundleName))
			return map;

		if (StringUtils.isBlank(prefix))
			prefix = "";
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(bundleName);
		} catch (MissingResourceException e) {
			log.warn("Resource [" + bundleName + "] not found, returning empty map");
			return map;
		}
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			if (key.startsWith(prefix))
				key = key.substring(prefix.length());
			else if (filterByPrefix)
				continue;
			if (toUpperKey)
				key = key.toUpperCase();

			map.put(key, value);
		}
		DebugUtils.dumpMap(logLevel, description, map);

		return map;
	}

	/**
	 * Checks if a bundle exists
	 * 
	 * @param bundleName bundle name to check
	 * @return true if the bundle exists
	 */
	public static boolean bundleExists(String bundleName) {

		try {
			ResourceBundle.getBundle(bundleName);
		} catch (MissingResourceException e) {
			log.warn("Resource [" + bundleName + "] does not exist");
			return false;
		}

		return true;
	}
}
