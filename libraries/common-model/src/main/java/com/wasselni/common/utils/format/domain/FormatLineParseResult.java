package com.wasselni.common.utils.format.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import com.wasselni.common.utils.DebugUtils;
import com.wasselni.common.utils.Utils;

public class FormatLineParseResult {

	private Map<String, Object> variableValues;
	private int lineNumber;

	public FormatLineParseResult() {
		variableValues = new HashMap<String, Object>();
	}

	public void copyFormatLineParseResult(FormatLineParseResult formatLineParseResult) {

		if (!Utils.emptyMap(formatLineParseResult.variableValues)) {
			if (Utils.emptyMap(variableValues))
				variableValues = new HashMap<String, Object>();
			variableValues.putAll(formatLineParseResult.variableValues);
		}

		this.lineNumber = formatLineParseResult.lineNumber;
	}

	/**
	 * Get a date object for the variable Name specified. If multiple variables with
	 * the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the date for
	 * @return Date object if found, null otherwise
	 */
	public Date getDate(String tagName) {

		Object o = getValue(tagName);
		if (o != null)
			return (Date) o;
		else
			return null;
	}

	/**
	 * Get a String object for the variable Name specified. If multiple variables
	 * with the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the String for
	 * @return String object if found, null otherwise
	 */
	public String getString(String tagName) {

		return getString(tagName, false);
	}

	/**
	 * Get a String object for the variable Name specified. If multiple variables
	 * with the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the String for
	 * @param trim    specifies if we should trim the value in case not null
	 * @return String object if found, null otherwise
	 */
	public String getString(String tagName, boolean trim) {

		Object o = getValue(tagName);
		if (o != null) {
			if (!trim)
				return String.valueOf(o);
			else
				return String.valueOf(o).trim();
		}

		else
			return null;
	}

	/**
	 * Get a Short object for the variable Name specified. If multiple variables
	 * with the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the Short for
	 * @return Short object if found, null otherwise
	 */
	public Short getShort(String tagName) {
		Object o = getValue(tagName);
		if (o == null)
			return null;
		else
			return Short.valueOf(String.valueOf(o));
	}

	/**
	 * Get an Integer object for the variable Name specified. If multiple variables
	 * with the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the Integer for
	 * @return Integer object if found, null otherwise
	 */
	public Integer getInt(String tagName) {
		Object o = getValue(tagName);
		if (o != null)
			return Integer.valueOf(String.valueOf(o));
		else
			return null;
	}

	/**
	 * Get a Long object for the variable Name specified. If multiple variables with
	 * the same name are found in the result, the first position will be used
	 * 
	 * @param tagName name of the tag to get the long for
	 * @return Long object if found, null otherwise
	 */
	public Long getLong(String tagName) {

		Object o = getValue(tagName);
		if (o != null)
			return Long.valueOf(String.valueOf(o));
		else
			return null;
	}

	/**
	 * Get a Big Decimal object for the variable Name specified. If multiple
	 * variables with the same name are found in the result, the first position will
	 * be used
	 * 
	 * @param tagName name of the tag to get the BigDecimal for
	 * @return BigDecimal object if found, null otherwise
	 */
	public BigDecimal getBigDecimal(String tagName) {
		Object o = getValue(tagName);
		if (o != null)
			return new BigDecimal(String.valueOf(o));
		else
			return null;
	}

	/**
	 * Returns the size of the results
	 * 
	 * @return size of the result (number of fields in a line parse result)
	 */
	public int size() {
		if (Utils.emptyMap(variableValues))
			return 0;
		else
			return variableValues.size();
	}

	/**
	 * Gets the object at index i
	 * 
	 * @param i index to get the object for. First position is 1
	 * @return the object value at the specified index
	 */
	/*
	 * private Object getValue(int i) { ParseResultEntry e = result.get(i - 1); if
	 * (e == null) return null; else return e.getValue(); }
	 */

	/**
	 * Gets the object with the tag name specified
	 * 
	 * @param tagName tag name of the variable to get the object value for. If
	 *                multiple variables have the same name, the first entry is
	 *                returned
	 * @return the value of the object if found, null otherwise
	 */
	private Object getValue(String tagName) {

		if (Utils.emptyMap(variableValues))
			return null;

		return variableValues.get(tagName);
	}

	public void dumpResult() {
		dumpResult(null);
	}

	/**
	 * Dumps the result
	 * 
	 * @param description description to show in the logs
	 */
	public void dumpResult(String description) {

		dumpResult(description, Level.INFO);
	}

	/**
	 * Dumps the result
	 * 
	 * @param description description to show in the logs
	 * @param level       debug level
	 */
	public void dumpResult(String description, Level level) {

		DebugUtils.dumpMap(description, variableValues);

	}

	/**
	 * Generates a human readable breakdown of the message
	 * 
	 * @param description description of the message
	 * @return breakdown of the message
	 */
	public String generateBreakdownLog(String description) {

		String out = "";

		if (StringUtils.isNotBlank(description))
			out += DebugUtils.generateDebugSpecial(description, "=");
		else
			out += DebugUtils.generateSeparator("=");

		out += "\n";

		if (!Utils.emptyMap(variableValues)) {

			for (Map.Entry<String, Object> entry : variableValues.entrySet()) {
				out += DebugUtils.generateTvWithLen(entry.getKey(), entry.getValue()) + "\n";
			}

		} else {
			out += "          Empty Parse Result\n";

		}

		out += DebugUtils.generateSeparator("=");

		return out + "\n\n";
	}

	/**
	 * Gets the list of variables values in a map format
	 * 
	 * @return map with the variables values
	 */
	public Map<String, Object> getVariableValues() {
		return variableValues;
	}

	/**
	 * Adds an entry to the result
	 * 
	 * @param tag   tag name to add the entry for.
	 * @param value value of the entry
	 */
	public void addParseResultEntry(FormatVar tag, Object value) {

		if (Utils.emptyMap(variableValues))
			variableValues = new HashMap<String, Object>();
		variableValues.put(tag.getName(), value);
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * Internal class holding the tag name and the value for each entry in a line
	 */
	public class ParseResultEntry {

		private FormatVar tagName;
		private Object value;

		public ParseResultEntry(FormatVar tagName, Object value) {
			super();
			this.tagName = tagName;
			this.value = value;
		}

		public FormatVar getTagName() {
			return tagName;
		}

		public void setTagName(FormatVar tagName) {
			this.tagName = tagName;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}
}
