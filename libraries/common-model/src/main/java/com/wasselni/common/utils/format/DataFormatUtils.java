package com.wasselni.common.utils.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.common.utils.DatabaseUtils;
import com.wasselni.common.utils.PciUtils;
import com.wasselni.common.utils.Utils;
import com.wasselni.common.utils.format.domain.FormatVar;
import com.wasselni.common.utils.format.domain.FormatVar.VT;
import com.wasselni.common.utils.format.xml.FormatFile;
import com.wasselni.common.utils.map.xml.GenericXmlMap;

/**
 * 
 * Utility functions for formatting data
 *
 */
public class DataFormatUtils {

	private static final Log log = LogFactory.getLog(DataFormatUtils.class);

	private static String VALID_PATTERN = "[0-9]+|[A-Z]+";

	/**
	 * Chops the pattern into individual parts to be used in the data generation.
	 * Each part will be the sequence of the same character which will be
	 * substituted by the same value i.e. <code>zzzzzzzwwLLLLLLk
	 * </code> will be chopped up to 4 sequences <code>{zzzzz}{ww}{LLLLLL}{k}</code>
	 * A list of supported separators can be specified. These separators will not
	 * break a sequence of characters. i.e. <code>zzzz zwwLLLLL Lk</code> with a
	 * list having the space as a separator will be chopped up to
	 * <code> {zzzz z}{ww}{LLLLL L}{k}</code>
	 * 
	 * @param format format pattern to chop-up
	 * @param list   of separators that will not break a sequence of characters
	 * @return list of strings chopped-up
	 */
	public static List<String> chopUpPattern(String pattern, List<String> supportedSeparators) {

		List<String> parts = new ArrayList<String>();

		if (StringUtils.isBlank(pattern)) {
			log.error("Empty pattern");
			return null;
		}

		char currentChar = '\0';
		int position = 0;
		String currentPart = "";

		while (pattern.length() > position) {
			if (pattern.charAt(position) != currentChar
					&& (Utils.emptyList(supportedSeparators) || (!Utils.emptyList(supportedSeparators) && !Utils
							.isInList("Supported Separators", supportedSeparators, pattern.charAt(position) + ""))
							&& !(supportedSeparators.contains(" ") && pattern.charAt(position) == ' '))) {
				if (StringUtils.isNotBlank(currentPart))
					parts.add(currentPart);
				currentChar = pattern.charAt(position);
				currentPart = "" + pattern.charAt(position);
			} else {
				currentPart += pattern.charAt(position);
			}
			position++;
		}

		if (StringUtils.isNotBlank(currentPart))
			parts.add(currentPart);

		log.debug("Chopped up pattern to " + parts);

		return parts;
	}

	/**
	 * Chops up a pattern returning an alternate list of numeric and alphanumeric
	 * values. i.e. if the pattern is 0000123123ASB123KKKK, the list returned will
	 * have 1st entry 0000123123 2nd entry ASB 3rd entry 123 4th entry KKKK
	 * 
	 * @param input
	 * @return
	 */
	public static List<String> getAlternateAlpaAndNumbericCharacters(String input) {

		List<String> chunks = new LinkedList<String>();
		input = input + "$"; // Added invalid character to force the last
								// chunk to be chopped off
		int beginIndex = 0;
		int endIndex = 0;
		while (endIndex < input.length()) {
			while (input.substring(beginIndex, endIndex + 1).matches(VALID_PATTERN)) {
				endIndex++;
			}
			if (beginIndex != endIndex) {
				chunks.add(input.substring(beginIndex, endIndex));
			} else {
				endIndex++;
			}
			beginIndex = endIndex;
		}
		return chunks;

	}

	/**
	 * Generates a record using the file format specified and substitution
	 * characters and variables specified
	 * 
	 * @param description        description of the record to be created
	 * @param file               format file holding the line configuration to be
	 *                           generated
	 * @param substitutionValues mapping between variable name and value to be used
	 *                           in the generation
	 * @param map                xml map to use to get a specific variable in case
	 *                           the variable had a map id specified for it
	 * @return output formatted string based on the file format
	 * @throws SystemException in case any of the values specified in the
	 *                         substitution values is invalid based on the format
	 *                         file configuration
	 */
	public static String generateRecordFromFormat(String description, FormatFile file,
			Map<String, Object> substitutionValues, GenericXmlMap map) throws SystemException {

		return generateRecordFromFormat(description, file, file.getVars(), substitutionValues, map);
	}

	/**
	 * Generates a record using the file format specified and substitution
	 * characters and variables specified
	 * 
	 * @param description        description of the record to be created
	 * @param file               format file holding the line configuration to be
	 *                           generated
	 * @param substitutionValues mapping between variable name and value to be used
	 *                           in the generation
	 * @param map                xml map to use to get a specific variable in case
	 *                           the variable had a map id specified for it
	 * @return output formatted string based on the file format
	 * @throws SystemException in case any of the values specified in the
	 *                         substitution values is invalid based on the format
	 *                         file configuration
	 */
	public static String generateRecordFromFormatForDebug(String description, FormatFile file,
			Map<String, Object> substitutionValues, GenericXmlMap map) throws SystemException {

		return generateRecordFromFormat(description, file, file.getVars(), substitutionValues, map, false, true);
	}

	/**
	 * Generates a record using the file format specified and substitution
	 * characters and variables specified
	 * 
	 * @param description        description of the record to be created
	 * @param file               format file holding the line configuration to be
	 *                           generated
	 * @param formatVars         format variables to be used to generate the line
	 * @param substitutionValues mapping between variable name and value to be used
	 *                           in the generation
	 * @param map                xml map to use to get a specific variable in case
	 *                           the variable had a map id specified for it
	 * @return output formatted string based on the file format
	 * @throws SystemException in case any of the values specified in the
	 *                         substitution values is invalid based on the format
	 *                         file configuration
	 */
	public static String generateRecordFromFormat(String description, FormatFile file, List<FormatVar> formatVars,
			Map<String, Object> substitutionValues, GenericXmlMap map) throws SystemException {

		return generateRecordFromFormat(description, file, formatVars, substitutionValues, map, false, false);

	}

	public static String generateRecordFromFormat(String description, FormatFile file, List<FormatVar> formatVars,
			Map<String, Object> substitutionValues, GenericXmlMap map, boolean useDatabaseFieldId)
			throws SystemException {

		return generateRecordFromFormat(description, file, formatVars, substitutionValues, map, useDatabaseFieldId,
				false);
	}

	/**
	 * Generates a record using the file format specified and substitution
	 * characters and variables specified
	 * 
	 * @param description        description of the record to be created
	 * @param file               format file holding the line configuration to be
	 *                           generated
	 * @param formatVars         format variables to be used to generate the line
	 * @param substitutionValues mapping between variable name and value to be used
	 *                           in the generation
	 * @param map                xml map to use to get a specific variable in case
	 *                           the variable had a map id specified for it
	 * @param useDatabaseFieldId specifies if the database field ID should be use to
	 *                           get the value from the map
	 * @param maskPan            mask pan
	 * @return output formatted string based on the file format
	 * @throws SystemException in case any of the values specified in the
	 *                         substitution values is invalid based on the format
	 *                         file configuration
	 */
	public static String generateRecordFromFormat(String description, FormatFile file, List<FormatVar> formatVars,
			Map<String, Object> substitutionValues, GenericXmlMap map, boolean useDatabaseFieldId, boolean maskPan)
			throws SystemException {

		String out = "";

		if (substitutionValues == null)
			throw new SystemException(SystemError.VE_MISSING_MANDATORY, "Substitution Values");

		if (file == null || Utils.emptyList(file.getVars())) {
			throw new SystemException(SystemError.VE_MISSING_MANDATORY, "Format Variables");
		}

		int i = 0;
		for (FormatVar formatVar : formatVars) {

			Object value = null;

			String key = formatVar.getName();
			if (useDatabaseFieldId)
				key = formatVar.getFieldName();

			if (StringUtils.isNotBlank(formatVar.getMapId()) && map != null) {
				value = map.getMapToValue(formatVar.getMapId(), getStringValue(substitutionValues.get(key), VT.String,
						null, formatVar.getDecimalNb(), formatVar.getName(), formatVar.getDecimalFormat()));
			} else {
				value = substitutionValues.get(key);
			}

			String valueString = getStringValue(value, formatVar.getType(), formatVar.getSdf(),
					formatVar.getDecimalNb(), formatVar.getName(), formatVar.getDecimalFormat());
			String defaultValue = getStringValue(formatVar.getDefaultValue(), formatVar.getType(), formatVar.getSdf(),
					formatVar.getDecimalNb(), formatVar.getName(), formatVar.getDecimalFormat());

			String entry = Utils.verifyMinMaxMandatory(valueString,
					formatVar.isAllowPad() ? null : formatVar.getMinLen(),
					formatVar.isAllowTrim() ? null : formatVar.getMaxLen(),
					StringUtils.isBlank(defaultValue) ? formatVar.isMandatory() : false, formatVar.getName(),
					defaultValue, formatVar.getAllowedValues());

			int minLength = formatVar.getMinLen() == null ? 0 : formatVar.getMinLen();

			int maxLength = formatVar.getMaxLen() == null ? 0 : formatVar.getMaxLen();

			if (file.getType().equals(FormatFile.fileTypeFixedLength))
				minLength = maxLength;

			if (formatVar.isMaskPan() && maskPan && StringUtils.isNotBlank(entry))
				entry = PciUtils.mask(entry);

			if (formatVar.isAllToUpper() && StringUtils.isNotBlank(entry))
				entry = entry.toUpperCase();

			if (formatVar.isAllToLower() && StringUtils.isNotBlank(entry))
				entry = entry.toLowerCase();

			String substitution = verifyAjustSubstitution(entry, minLength, maxLength, formatVar.isAllowTrim(),
					formatVar.isTrimRight(), formatVar.isAllowPad(), formatVar.getPaddingChar().charAt(0),
					formatVar.isPadRight(), !formatVar.isMandatory(), description, formatVar.getName(),
					formatVar.getType(), formatVar.getAllowedSeperator(), formatVar.getPatternPart(),
					file.getType().equals(FormatFile.fileTypeDelimited) ? formatVar.getPrefix() : null,
					file.getType().equals(FormatFile.fileTypeDelimited) ? formatVar.getSuffix() : null);

			if (!StringUtils.isBlank(formatVar.getLengthFormat())) {
				int valLength = substitution.length();
				out += StringUtils.leftPad(Integer.toString(valLength), formatVar.getLengthFormat().length(), '0');
			}

			out += substitution;

			if (StringUtils.isNotBlank(file.getDelimiter())
					&& (i < formatVars.size() - 1 || (file.getAppendSeperator() != null && file.getAppendSeperator())))
				out += file.getDelimiter();

			++i;
		}

		if (file.getEndOfLine() != null && file.getEndOfLine() != "")
			out += file.getEndOfLine();

		return out;
	}

	/**
	 * Gets the string value of the object value specified
	 * 
	 * @param value         object value to convert to string
	 * @param type          type of the object value to convert
	 * @param sdf           simple date format to be used to convert a date value
	 * @param decimalNb     decimal number to remove from the variable
	 * @param description   description of the variable
	 * @param decimalFormat
	 * @return String value of the object specified
	 * @throws SystemException in case of incompatible types
	 */
	private static String getStringValue(Object value, VT type, SimpleDateFormat sdf, Integer decimalNb,
			String description, DecimalFormat decimalFormat) throws SystemException {

		String valueString = "";

		if (value != null) {

			if (type == VT.BLOB) {
				valueString = DatabaseUtils.blobToString(value);
			} else if (type == VT.CLOB) {
				valueString = DatabaseUtils.clobToString(value);
			} else if (type == VT.Date) {

				if (!(value instanceof Date)) {
					throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value, description + "/Date");
				} else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sdf.toPattern());
					valueString = simpleDateFormat.format((Date) value);
				}
			} else if (type == VT.Float) {
				if (value instanceof String)
					value = new BigDecimal((String) value);
				if (decimalFormat != null && value instanceof BigDecimal) {
					valueString = decimalFormat.format(value);
				} else if (null != decimalNb && decimalNb != 0 && value instanceof BigDecimal) {
					BigDecimal valueDbl = ((BigDecimal) value).multiply((BigDecimal.valueOf(Math.pow(10, decimalNb))
							.setScale(decimalNb, BigDecimal.ROUND_HALF_UP)));
					valueString = (valueDbl).longValue() + "";
				} else if (null != decimalNb && decimalNb != 0) {
					BigDecimal valueDbl = new BigDecimal(value.toString()).multiply((BigDecimal
							.valueOf(Math.pow(10, decimalNb)).setScale(decimalNb, BigDecimal.ROUND_HALF_UP)));
					valueString = (valueDbl).longValue() + "";

				} else {
					if (value instanceof BigDecimal) {
						valueString = ((BigDecimal) value).toPlainString() + "";

					} else {
						try {
							new Double(value.toString());
							valueString = value.toString();
						} catch (NumberFormatException nex) {
							throw new SystemException(SystemError.VE_INVALID_FIELD_VALUE, value,
									description + "/Float Value");
						}
					}
				}
			} else {
				valueString = String.valueOf(value);
			}
		}

		return valueString;
	}

	/**
	 * Verifies and formats the value passed to it based on the options specified
	 * 
	 * @param value        value to format/adjust
	 * @param length       length required for the output
	 * @param trim         specifies if trim is allowed for the value
	 * @param trimRight    specifies if the value should be trimmed from the right
	 *                     if required and allowed
	 * @param pad          specifies if pad is allowed for the value
	 * @param paddingChar  specifies the char to use for the padding in case
	 *                     required and padding allowed
	 * @param padRight     specifies if the value should be padded to the right if
	 *                     required and allowed
	 * @param allowEmpty   specifies if the value is allowed to be empty
	 * @param descr        description of the formatting (i.e. track generation,
	 *                     account number generation...) used for the debugging
	 * @param field        field name being formatted, used for the debugging
	 * @param variableType variable type (might be used for numbers formatting in
	 *                     case of negative values)
	 * @return the formatted string
	 * @throws SystemException in case of an invalid value
	 */
	public static String verifyAjustSubstitution(String value, int length, boolean trim, boolean trimRight, boolean pad,
			char paddingChar, boolean padRight, boolean allowEmpty, String descr, String field, VT variableType)
			throws SystemException {

		return verifyAjustSubstitution(value, length, trim, trimRight, pad, paddingChar, padRight, allowEmpty, descr,
				field, variableType, null, null, null, null);
	}

	/**
	 * Verifies and formats the value passed to it based on the options specified
	 * 
	 * @param value       value to format/adjust
	 * @param length      length required for the output
	 * @param trim        specifies if trim is allowed for the value
	 * @param trimRight   specifies if the value should be trimmed from the right if
	 *                    required and allowed
	 * @param pad         specifies if pad is allowed for the value
	 * @param paddingChar specifies the char to use for the padding in case required
	 *                    and padding allowed
	 * @param padRight    specifies if the value should be padded to the right if
	 *                    required and allowed
	 * @param allowEmpty  specifies if the value is allowed to be empty
	 * @param descr       description of the formatting (i.e. track generation,
	 *                    account number generation...) used for the debugging
	 * @param field       field name being formatted, used for the debugging
	 * @param separators  list of separators that are not considered as breaking a
	 *                    pattern char sequence. Used to insert the same separator
	 *                    in the output string
	 * @param patternPart original pattern part to get the positions of separators
	 *                    from and insert them in the output string. i.e. if the
	 *                    pattern part is {zzz z} and the equivalent value is {4444}
	 *                    and the separators include space, the output will be {444
	 *                    4}
	 * @return the formatted string
	 * @throws SystemException in case of an invalid value
	 */
	public static String verifyAjustSubstitution(String value, int length, boolean trim, boolean trimRight, boolean pad,
			char paddingChar, boolean padRight, boolean allowEmpty, String descr, String field, List<String> separators,
			String patternPart) throws SystemException {

		return verifyAjustSubstitution(value, length, trim, trimRight, pad, paddingChar, padRight, allowEmpty, descr,
				field, null, separators, patternPart, null, null);
	}

	public static String verifyAjustSubstitution(String value, int length, boolean trim, boolean trimRight, boolean pad,
			char paddingChar, boolean padRight, boolean allowEmpty, String descr, String field, VT type,
			List<String> separators, String patternPart) throws SystemException {

		return verifyAjustSubstitution(value, length, trim, trimRight, pad, paddingChar, padRight, allowEmpty, descr,
				field, null, separators, patternPart, null, null);
	}

	/**
	 * Verifies and formats the value passed to it based on the options specified
	 * 
	 * @param value        value to format/adjust
	 * @param length       length required for the output
	 * @param trim         specifies if trim is allowed for the value
	 * @param trimRight    specifies if the value should be trimmed from the right
	 *                     if required and allowed
	 * @param pad          specifies if pad is allowed for the value
	 * @param paddingChar  specifies the char to use for the padding in case
	 *                     required and padding allowed
	 * @param padRight     specifies if the value should be padded to the right if
	 *                     required and allowed
	 * @param allowEmpty   specifies if the value is allowed to be empty
	 * @param descr        description of the formatting (i.e. track generation,
	 *                     account number generation...) used for the debugging
	 * @param field        field name being formatted, used for the debugging
	 * @param variableType type of the variable (if numeric or not to be able to
	 *                     handle the padding)
	 * @param separators   list of separators that are not considered as breaking a
	 *                     pattern char sequence. Used to insert the same separator
	 *                     in the output string
	 * @param patternPart  original pattern part to get the positions of separators
	 *                     from and insert them in the output string. i.e. if the
	 *                     pattern part is {zzz z} and the equivalent value is
	 *                     {4444} and the separators include space, the output will
	 *                     be {444 4}
	 * @return the formatted string
	 * @throws SystemException in case of an invalid value
	 */
	public static String verifyAjustSubstitution(String value, int length, boolean trim, boolean trimRight, boolean pad,
			char paddingChar, boolean padRight, boolean allowEmpty, String descr, String field, VT type,
			List<String> separators, String patternPart, String prefix, String suffix) throws SystemException {

		return verifyAjustSubstitution(value, length, length, trim, trimRight, pad, paddingChar, padRight, allowEmpty,
				descr, field, type, separators, patternPart, prefix, suffix);
	}

	/**
	 * Verifies and formats the value passed to it based on the options specified
	 * 
	 * @param value        value to format/adjust
	 * @param minlength    minimum length required for the output
	 * @param maxlength    maximum length required for the output
	 * @param trim         specifies if trim is allowed for the value
	 * @param trimRight    specifies if the value should be trimmed from the right
	 *                     if required and allowed
	 * @param pad          specifies if pad is allowed for the value
	 * @param paddingChar  specifies the char to use for the padding in case
	 *                     required and padding allowed
	 * @param padRight     specifies if the value should be padded to the right if
	 *                     required and allowed
	 * @param allowEmpty   specifies if the value is allowed to be empty
	 * @param descr        description of the formatting (i.e. track generation,
	 *                     account number generation...) used for the debugging
	 * @param field        field name being formatted, used for the debugging
	 * @param variableType type of the variable (if numeric or not to be able to
	 *                     handle the padding)
	 * @param separators   list of separators that are not considered as breaking a
	 *                     pattern char sequence. Used to insert the same separator
	 *                     in the output string
	 * @param patternPart  original pattern part to get the positions of separators
	 *                     from and insert them in the output string. i.e. if the
	 *                     pattern part is {zzz z} and the equivalent value is
	 *                     {4444} and the separators include space, the output will
	 *                     be {444 4}
	 * @return the formatted string
	 * @throws SystemException in case of an invalid value
	 */
	public static String verifyAjustSubstitution(String value, int minlength, int maxlength, boolean trim,
			boolean trimRight, boolean pad, char paddingChar, boolean padRight, boolean allowEmpty, String descr,
			String field, VT type, List<String> separators, String patternPart, String prefix, String suffix)
			throws SystemException {

		String output = value;
		int length = output != null ? output.getBytes().length : 0;

		// Check if the value is empty. If it is and empty is not allowed, treat
		// as an error
		if (StringUtils.isBlank(value) && !allowEmpty) {
			throw new SystemException(SystemError.DF_PATTERN_SPECIFIES_FIELD_NOT_FOUND, descr, field);
		}

		// If the pattern part has spaces, insert spaces in the actual values at
		// the same positions as in the part
		if (!Utils.emptyList(separators) && StringUtils.isNotBlank(patternPart)) {
			for (int i = 0; i < patternPart.length(); i++) {
				if ((patternPart.charAt(i) == ' '
						|| Utils.isInList("Pattern Separator", separators, "" + patternPart.charAt(i)))
						&& output.length() >= i) {
					output = output.substring(0, i) + patternPart.charAt(i) + output.substring(i);
					log.trace("new output " + output);
					length = output.getBytes().length;
				}
			}
		}

		// if the length required is specified
		// if the length of the value is greater than the one needed and
		// trim is not allowed, treat as an error, otherwise trim the value
		if (maxlength > 0 && length > maxlength) {
			if (!trim)
				throw new SystemException(SystemError.DF_PATTERN_INVALID_FIELD_LENGTH, descr, field, output.length(),
						"greater", maxlength);
			else {
				if (trimRight) {

					output = new String(Arrays.copyOf(output.getBytes(), maxlength));

				} else {
					output = new String(Arrays.copyOfRange(output.getBytes(), output.getBytes().length - maxlength,
							output.getBytes().length));
				}
			}
		}

		// if the length of the value is less than the one needed and pad is
		// not allowed, treat as an error, otherwise pad the value with the
		// character provided to the function
		if (minlength > 0 && length < minlength) {
			if (!pad)
				throw new SystemException(SystemError.DF_PATTERN_INVALID_FIELD_LENGTH, descr, field, output.length(),
						"less", minlength);
			else {
				String padding = StringUtils.leftPad("", minlength - length, paddingChar);
				if (padRight) {
					if (output != null)
						output = new String(ArrayUtils.addAll(output.getBytes(), padding.getBytes()));
					else
						output = new String(padding.getBytes());

				}
				// if pad left
				else {
					// if is not null
					if (length != 0) {
						// if is number
						if (type != null
								&& (type == VT.Float || type == VT.Integer || type == VT.Long || type == VT.Short)) {

							// if first val is sign
							String negativeSignVal = "-";
							String positiveSignVal = "+";
							String firstVar = output.substring(0, 1);
							if (firstVar.equalsIgnoreCase(negativeSignVal)
									|| firstVar.equalsIgnoreCase(positiveSignVal)) {

								output = output.substring(1);

								output = new String(ArrayUtils.addAll(padding.getBytes(), output.getBytes()));

								output = firstVar + output;

							} else {
								output = new String(ArrayUtils.addAll(padding.getBytes(), output.getBytes()));
							}
						} else {
							output = new String(ArrayUtils.addAll(padding.getBytes(), output.getBytes()));
						}
					} else {
						output = new String(padding.getBytes());
					}
				}
			}
		}

		if (StringUtils.isNotBlank(prefix))
			output = prefix.concat(output);

		if (StringUtils.isNotBlank(suffix))
			output = output.concat(suffix);

		return output;
	}

}
