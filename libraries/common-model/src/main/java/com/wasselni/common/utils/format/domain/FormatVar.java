package com.wasselni.common.utils.format.domain;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 
 * Domain object holding the options for a variable entry in a line
 *
 */
public class FormatVar implements Comparable<FormatVar> {

	public enum VT {
		String, Short, Integer, Long, Date, Float, Pan, CLOB, BLOB, COBOL;
	};

	private String name;
	private String mapId;
	private VT type;
	private Integer minLen;
	private Integer maxLen;
	private boolean mandatory;
	private int order;
	private Object defaultValue;
	private List<?> allowedValues;
	private SimpleDateFormat sdf;
	private boolean allowTrim;
	private boolean trimRight;
	private boolean allowPad;
	private String paddingChar;
	private boolean padRight;
	private String fieldName;
	private Integer decimalNb;
	private boolean allToUpper;
	private boolean allToLower;
	private String patternPart;
	private List<String> allowedSeperator;
	private boolean trimSpaces;
	private String currencyVariableName;
	private boolean currencyVariableAlpha;
	private String currencyVerificationMethod;
	private String countryVerificationMethod;
	private short fileVersion;
	private boolean trimAllSpaces;
	private boolean keepEmtpy;
	private String lengthFormat;
	private String prefix;
	private String suffix;
	private DecimalFormat decimalFormat;
	private boolean maskPan;

	/**
	 * Constructor that fills the required details for the variable
	 * 
	 * @param name                  variable name
	 * @param mapId                 id of the map to use in case specified.
	 * @param type                  type of the variable
	 * @param minLen                minimum length of the variable
	 * @param maxLen                maximum length of the variable
	 * @param mandatory             specifies if the variable is mandatory in a line
	 * @param order                 specifies the order of the variable in a line
	 * @param defaultValue          specifies the default value of the variable to
	 *                              be used in case it was empty in the line
	 * @param allowedValues         list of allowed values for this variable
	 * @param sdf                   simple date format used to parse the variable
	 *                              from the file in case the type is date
	 * @param allowTrim             specifies if trimming is allowed for the
	 *                              variable in case it has a length greater than
	 *                              the maximum
	 * @param trimRight             specifies if the variable needs to be trimmed,
	 *                              it will be trimmed from the right
	 * @param allowPad              specifies if padding is allowed for the variable
	 *                              in case it has a length less than the minimum
	 * @param paddingChar           specifies the char to use for the padding if
	 *                              required
	 * @param padRight              specifies if the variable should be padded to
	 *                              the right in case required
	 * @param fieldName             name of the field to use
	 * @param decimalNb             specifies the decimal number in case of decimal
	 *                              variables
	 * @param allToUpper            specifies if the output should be converted to
	 *                              upper case
	 * @param allToLower            specifies if the output should be converted to
	 *                              lower case
	 * @param patternPart           pattern used for formatting an entry
	 * @param allowedSeperator      separators that are not considered a
	 *                              substitution
	 * @param trimSpaces            specifies if spaces should be trimmed for string
	 *                              variables
	 * @param currencyVariable      specifies the currency variable name to be used
	 *                              for decimal places handling of float variables
	 *                              if required
	 * @param currencyVariableAlpha specifies if the currency variable will have the
	 *                              currency in alpha format
	 * @param fileVersion           file version
	 */
	public FormatVar(String name, String mapId, VT type, Integer minLen, Integer maxLen, boolean mandatory, int order,
			Object defaultValue, List<?> allowedValues, SimpleDateFormat sdf, boolean allowTrim, boolean trimRight,
			boolean allowPad, String paddingChar, boolean padRight, String fieldName, int decimalNb, boolean allToUpper,
			boolean allToLower, String patternPart, List<String> allowedSeperator, boolean trimSpaces,
			String currencyVariable, boolean currencyVariableAlpha, String currencyVerificationMethod,
			String countryVerificationMethod, short fileVersion) {

		initFormatVar(name, mapId, type, minLen, maxLen, mandatory, order, defaultValue, allowedValues, sdf, allowTrim,
				trimRight, allowPad, paddingChar, padRight, fieldName, decimalNb, allToUpper, allToLower, patternPart,
				allowedSeperator, trimSpaces, currencyVariable, currencyVariableAlpha, currencyVerificationMethod,
				countryVerificationMethod, fileVersion, false, false, null, null, null, null, false);

	}

	/**
	 * Constructor that fills the required details for the variable
	 * 
	 * @param name                  variable name
	 * @param mapId                 id of the map to use in case specified.
	 * @param type                  type of the variable
	 * @param minLen                minimum length of the variable
	 * @param maxLen                maximum length of the variable
	 * @param mandatory             specifies if the variable is mandatory in a line
	 * @param order                 specifies the order of the variable in a line
	 * @param defaultValue          specifies the default value of the variable to
	 *                              be used in case it was empty in the line
	 * @param allowedValues         list of allowed values for this variable
	 * @param sdf                   simple date format used to parse the variable
	 *                              from the file in case the type is date
	 * @param allowTrim             specifies if trimming is allowed for the
	 *                              variable in case it has a length greater than
	 *                              the maximum
	 * @param trimRight             specifies if the variable needs to be trimmed,
	 *                              it will be trimmed from the right
	 * @param allowPad              specifies if padding is allowed for the variable
	 *                              in case it has a length less than the minimum
	 * @param paddingChar           specifies the char to use for the padding if
	 *                              required
	 * @param padRight              specifies if the variable should be padded to
	 *                              the right in case required
	 * @param fieldName             name of the field to use
	 * @param decimalNb             specifies the decimal number in case of decimal
	 *                              variables
	 * @param allToUpper            specifies if the output should be converted to
	 *                              upper case
	 * @param allToLower            specifies if the output should be converted to
	 *                              lower case
	 * @param patternPart           pattern used for formatting an entry
	 * @param allowedSeperator      separators that are not considered a
	 *                              substitution
	 * @param trimSpaces            specifies if spaces should be trimmed for string
	 *                              variables
	 * @param currencyVariable      specifies the currency variable name to be used
	 *                              for decimal places handling of float variables
	 *                              if required
	 * @param currencyVariableAlpha specifies if the currency variable will have the
	 *                              currency in alpha format
	 * @param fileVersion           file version
	 * @param trimAllSpaces         specifies if all spaces should be trimmed
	 *                              (removed)
	 * @param keepEmpty             keepField empty if null
	 */
	public FormatVar(String name, String mapId, VT type, Integer minLen, Integer maxLen, boolean mandatory, int order,
			Object defaultValue, List<?> allowedValues, SimpleDateFormat sdf, boolean allowTrim, boolean trimRight,
			boolean allowPad, String paddingChar, boolean padRight, String fieldName, int decimalNb, boolean allToUpper,
			boolean allToLower, String patternPart, List<String> allowedSeperator, boolean trimSpaces,
			String currencyVariable, boolean currencyVariableAlpha, String currencyVerificationMethod,
			String countryVerificationMethod, short fileVersion, boolean trimAllSpaces) {

		initFormatVar(name, mapId, type, minLen, maxLen, mandatory, order, defaultValue, allowedValues, sdf, allowTrim,
				trimRight, allowPad, paddingChar, padRight, fieldName, decimalNb, allToUpper, allToLower, patternPart,
				allowedSeperator, trimSpaces, currencyVariable, currencyVariableAlpha, currencyVerificationMethod,
				countryVerificationMethod, fileVersion, trimAllSpaces, false, null, null, null, null, false);
	}

	/**
	 * Constructor that fills the required details for the variable
	 * 
	 * @param name                  variable name
	 * @param mapId                 id of the map to use in case specified.
	 * @param type                  type of the variable
	 * @param minLen                minimum length of the variable
	 * @param maxLen                maximum length of the variable
	 * @param mandatory             specifies if the variable is mandatory in a line
	 * @param order                 specifies the order of the variable in a line
	 * @param defaultValue          specifies the default value of the variable to
	 *                              be used in case it was empty in the line
	 * @param allowedValues         list of allowed values for this variable
	 * @param sdf                   simple date format used to parse the variable
	 *                              from the file in case the type is date
	 * @param allowTrim             specifies if trimming is allowed for the
	 *                              variable in case it has a length greater than
	 *                              the maximum
	 * @param trimRight             specifies if the variable needs to be trimmed,
	 *                              it will be trimmed from the right
	 * @param allowPad              specifies if padding is allowed for the variable
	 *                              in case it has a length less than the minimum
	 * @param paddingChar           specifies the char to use for the padding if
	 *                              required
	 * @param padRight              specifies if the variable should be padded to
	 *                              the right in case required
	 * @param fieldName             name of the field to use
	 * @param decimalNb             specifies the decimal number in case of decimal
	 *                              variables
	 * @param allToUpper            specifies if the output should be converted to
	 *                              upper case
	 * @param allToLower            specifies if the output should be converted to
	 *                              lower case
	 * @param patternPart           pattern used for formatting an entry
	 * @param allowedSeperator      separators that are not considered a
	 *                              substitution
	 * @param trimSpaces            specifies if spaces should be trimmed for string
	 *                              variables
	 * @param currencyVariable      specifies the currency variable name to be used
	 *                              for decimal places handling of float variables
	 *                              if required
	 * @param currencyVariableAlpha specifies if the currency variable will have the
	 *                              currency in alpha format
	 * @param fileVersion           file version
	 * @param trimAllSpaces         specifies if all spaces should be trimmed
	 *                              (removed)
	 * @param keepEmpty             keepField empty if null
	 */
	public FormatVar(String name, String mapId, VT type, Integer minLen, Integer maxLen, boolean mandatory, int order,
			Object defaultValue, List<?> allowedValues, SimpleDateFormat sdf, boolean allowTrim, boolean trimRight,
			boolean allowPad, String paddingChar, boolean padRight, String fieldName, int decimalNb, boolean allToUpper,
			boolean allToLower, String patternPart, List<String> allowedSeperator, boolean trimSpaces,
			String currencyVariable, boolean currencyVariableAlpha, String currencyVerificationMethod,
			String countryVerificationMethod, short fileVersion, boolean trimAllSpaces, boolean keepEmpty,
			String lengthFormat, String prefix, String suffix, DecimalFormat decimalFormat, boolean maskPan) {

		initFormatVar(name, mapId, type, minLen, maxLen, mandatory, order, defaultValue, allowedValues, sdf, allowTrim,
				trimRight, allowPad, paddingChar, padRight, fieldName, decimalNb, allToUpper, allToLower, patternPart,
				allowedSeperator, trimSpaces, currencyVariable, currencyVariableAlpha, currencyVerificationMethod,
				countryVerificationMethod, fileVersion, trimAllSpaces, keepEmpty, lengthFormat, prefix, suffix,
				decimalFormat, maskPan);
	}

	/**
	 * initializes the format variable
	 * 
	 * @param name                  variable name
	 * @param mapId                 id of the map to use in case specified.
	 * @param type                  type of the variable
	 * @param minLen                minimum length of the variable
	 * @param maxLen                maximum length of the variable
	 * @param mandatory             specifies if the variable is mandatory in a line
	 * @param order                 specifies the order of the variable in a line
	 * @param defaultValue          specifies the default value of the variable to
	 *                              be used in case it was empty in the line
	 * @param allowedValues         list of allowed values for this variable
	 * @param sdf                   simple date format used to parse the variable
	 *                              from the file in case the type is date
	 * @param allowTrim             specifies if trimming is allowed for the
	 *                              variable in case it has a length greater than
	 *                              the maximum
	 * @param trimRight             specifies if the variable needs to be trimmed,
	 *                              it will be trimmed from the right
	 * @param allowPad              specifies if padding is allowed for the variable
	 *                              in case it has a length less than the minimum
	 * @param paddingChar           specifies the char to use for the padding if
	 *                              required
	 * @param padRight              specifies if the variable should be padded to
	 *                              the right in case required
	 * @param fieldName             name of the field to use
	 * @param decimalNb             specifies the decimal number in case of decimal
	 *                              variables
	 * @param allToUpper            specifies if the output should be converted to
	 *                              upper case
	 * @param allToLower            specifies if the output should be converted to
	 *                              lower case
	 * @param patternPart           pattern used for formatting an entry
	 * @param allowedSeperator      separators that are not considered a
	 *                              substitution
	 * @param trimSpaces            specifies if spaces should be trimmed for string
	 *                              variables
	 * @param currencyVariable      specifies the currency variable name to be used
	 *                              for decimal places handling of float variables
	 *                              if required
	 * @param currencyVariableAlpha specifies if the currency variable will have the
	 *                              currency in alpha format
	 * @param fileVersion           file version
	 * @param trimAllSpaces         specifies if all spaces should be trimmed
	 *                              (removed)
	 */
	public void initFormatVar(String name, String mapId, VT type, Integer minLen, Integer maxLen, boolean mandatory,
			int order, Object defaultValue, List<?> allowedValues, SimpleDateFormat sdf, boolean allowTrim,
			boolean trimRight, boolean allowPad, String paddingChar, boolean padRight, String fieldName, int decimalNb,
			boolean allToUpper, boolean allToLower, String patternPart, List<String> allowedSeperator,
			boolean trimSpaces, String currencyVariable, boolean currencyVariableAlpha,
			String currencyVerificationMethod, String countryVerificationMethod, short fileVersion,
			boolean trimAllSpaces, boolean keepEmpty, String lengthFormat, String prefix, String suffix,
			DecimalFormat decimalFormat, boolean maskPan) {
		this.name = name;
		this.type = type;
		this.minLen = minLen;
		this.maxLen = maxLen;
		this.mandatory = mandatory;
		this.order = order;
		this.defaultValue = defaultValue;
		this.allowedValues = allowedValues;
		this.sdf = sdf;
		this.allowTrim = allowTrim;
		this.trimRight = trimRight;
		this.allowPad = allowPad;
		this.paddingChar = paddingChar;
		this.padRight = padRight;
		this.mapId = mapId;
		this.fieldName = fieldName;
		this.decimalNb = decimalNb;
		this.allToUpper = allToUpper;
		this.allToLower = allToLower;
		this.patternPart = patternPart;
		this.allowedSeperator = allowedSeperator;
		this.trimSpaces = trimSpaces;
		this.currencyVariableName = currencyVariable;
		this.currencyVariableAlpha = currencyVariableAlpha;
		this.currencyVerificationMethod = currencyVerificationMethod;
		this.countryVerificationMethod = countryVerificationMethod;
		this.fileVersion = fileVersion;
		this.trimAllSpaces = trimAllSpaces;
		this.keepEmtpy = keepEmpty;
		this.lengthFormat = lengthFormat;
		this.prefix = prefix;
		this.suffix = suffix;
		this.decimalFormat = decimalFormat;
		this.maskPan = maskPan;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VT getType() {
		return type;
	}

	public void setType(VT type) {
		this.type = type;
	}

	public Integer getMinLen() {
		return minLen;
	}

	public void setMinLen(Integer minLen) {
		this.minLen = minLen;
	}

	public Integer getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(Integer maxLen) {
		this.maxLen = maxLen;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<?> getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(List<?> allowedValues) {
		this.allowedValues = allowedValues;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public boolean isAllowTrim() {
		return allowTrim;
	}

	public void setAllowTrim(boolean allowTrim) {
		this.allowTrim = allowTrim;
	}

	public boolean isTrimRight() {
		return trimRight;
	}

	public void setTrimRight(boolean trimRight) {
		this.trimRight = trimRight;
	}

	public boolean isAllowPad() {
		return allowPad;
	}

	public void setAllowPad(boolean allowPad) {
		this.allowPad = allowPad;
	}

	public String getPaddingChar() {
		return paddingChar;
	}

	public void setPaddingChar(String paddingChar) {
		this.paddingChar = paddingChar;
	}

	public boolean isPadRight() {
		return padRight;
	}

	public void setPadRight(boolean padRight) {
		this.padRight = padRight;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Integer getDecimalNb() {
		return decimalNb;
	}

	public void setDecimalNb(Integer decimalNb) {
		this.decimalNb = decimalNb;
	}

	public boolean isAllToUpper() {
		return allToUpper;
	}

	public void setAllToUpper(boolean allToUpper) {
		this.allToUpper = allToUpper;
	}

	public boolean isAllToLower() {
		return allToLower;
	}

	public void setAllToLower(boolean allToLower) {
		this.allToLower = allToLower;
	}

	public String getPatternPart() {
		return patternPart;
	}

	public void setPatternPart(String patternPart) {
		this.patternPart = patternPart;
	}

	public List<String> getAllowedSeperator() {
		return allowedSeperator;
	}

	public void setAllowedSeperator(List<String> allowedSeperator) {
		this.allowedSeperator = allowedSeperator;
	}

	public boolean isTrimSpaces() {
		return trimSpaces;
	}

	public boolean isTrimAllSpaces() {
		return trimAllSpaces;
	}

	public String getCurrencyVariableName() {
		return currencyVariableName;
	}

	public void setCurrencyVariableName(String currencyVariableName) {
		this.currencyVariableName = currencyVariableName;
	}

	public void setTrimSpaces(boolean trimSpaces) {
		this.trimSpaces = trimSpaces;
	}

	public boolean isCurrencyVariableAlpha() {
		return currencyVariableAlpha;
	}

	public void setCurrencyVariableAlpha(boolean currencyVariableAlpha) {
		this.currencyVariableAlpha = currencyVariableAlpha;
	}

	public String getCurrencyVerificationMethod() {
		return currencyVerificationMethod;
	}

	public void setCurrencyVerificationMethod(String currencyVerificationMethod) {
		this.currencyVerificationMethod = currencyVerificationMethod;
	}

	public String getCountryVerificationMethod() {
		return countryVerificationMethod;
	}

	public void setCountryVerificationMethod(String countryVerificationMethod) {
		this.countryVerificationMethod = countryVerificationMethod;
	}

	public short getFileVersion() {
		return fileVersion;
	}

	public boolean isKeepEmtpy() {
		return keepEmtpy;
	}

	public void setKeepEmtpy(boolean keepEmtpy) {
		this.keepEmtpy = keepEmtpy;
	}

	public String getLengthFormat() {
		return lengthFormat;
	}

	public void setLengthFormat(String lengthFormat) {
		this.lengthFormat = lengthFormat;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isMaskPan() {
		return maskPan;
	}

	public void setMaskPan(boolean maskPan) {
		this.maskPan = maskPan;
	}

	public void setFileVersion(short fileVersion) {
		this.fileVersion = fileVersion;
	}

	public void setTrimAllSpaces(boolean trimAllSpaces) {
		this.trimAllSpaces = trimAllSpaces;
	}

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	@Override
	public int compareTo(FormatVar o) {
		if (this.order < o.getOrder())
			return -1;
		else if (this.order > o.getOrder())
			return 1;
		return 0;
	}

}
