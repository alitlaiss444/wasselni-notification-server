package com.wasselni.common.utils.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.common.utils.DebugUtils;
import com.wasselni.common.utils.Utils;
import com.wasselni.common.utils.format.domain.FormatLineParseResult;
import com.wasselni.common.utils.format.domain.FormatVar;
import com.wasselni.common.utils.format.domain.FormatVar.VT;
import com.wasselni.common.utils.format.xml.FormatFile;
import com.wasselni.common.utils.format.xml.UniqueKey;

/**
 * 
 * Parses a line based on a format file
 *
 */
public class FormatLineParser {

	private static final Log log = LogFactory.getLog(FormatLineParser.class);

	private boolean fixedLength;
	private String separator;
	private List<FormatVar> variables;
	private int fixedLengthVariablesLength;
	private List<FormatVar> headerVariables;
	private int fixedLengthHeaderVariablesLength;
	private List<FormatVar> footerVariables;
	private int fixedLengthFooterVariablesLength;
	private List<UniqueKey> uniqueKeys;
	private boolean appendSeparator;
	private boolean debugMode;
	private boolean migrateDb;
	private String tableName;
	private String headerTableName;
	private String footerTableName;
	private String endOfLine;
	private boolean checkLengthInBytes;
	private boolean skipLengthCheck;

	private FormatFile formatFile;

	/**
	 * Maps a string line and generates a format line parse result with the data
	 * filled
	 * 
	 * @param line line to parse
	 * @return the parsing result of the line
	 * @throws SystemException
	 */
	public FormatLineParseResult mapLine(String line, int lineNumber) throws SystemException {

		return mapLine(line, this.variables, fixedLengthVariablesLength, lineNumber);

	}

	/**
	 * Maps a string header to a format line parse result with the values filled
	 * 
	 * @param line header line to parse
	 * @return parse result of the header
	 * @throws SystemException
	 */
	public FormatLineParseResult mapHeaderLine(String line) throws SystemException {

		return mapLine(line, headerVariables, fixedLengthHeaderVariablesLength);

	}

	/**
	 * Maps a string footer to a format line parse result with the values filled
	 * 
	 * @param line header line to parse
	 * @return parse result of the header
	 * @throws SystemException
	 */
	public FormatLineParseResult mapFooterLine(String line) throws SystemException {

		return mapLine(line, this.footerVariables, fixedLengthFooterVariablesLength);
	}

	/**
	 * ==================================================================== Actual
	 * Processing
	 * ===================================================================
	 */
	/**
	 * Formats a line based on the variables passed to it
	 * 
	 * @param line        line to map
	 * @param variables   variables to use for the mapping
	 * @param fixedLength fixed length expected for the line to avoid the loop on
	 *                    the variables and get the length every time
	 * @return parse result of the line based on the specified variables
	 * @throws SystemException
	 */
	private FormatLineParseResult mapLine(String line, List<FormatVar> variables, int fixedLength)
			throws SystemException {

		return mapLine(line, variables, fixedLength, -1);

	}

	/**
	 * Formats a line based on the variables passed to it
	 * 
	 * @param line        line to format/parse
	 * @param variables   variables based on which the line should be parsed
	 * @param fixedLength fixed length of the line in case the format is fixed
	 *                    length to avoid the loop every time to get the maximum
	 *                    length
	 * @param lineNumber  line number being formatted/parsed
	 * @return parse result of the line based on the specified variables
	 * @throws SystemException
	 */
	private FormatLineParseResult mapLine(String line, List<FormatVar> variables, int fixedLength, Integer lineNumber)
			throws SystemException {

		if (StringUtils.isBlank(line)) {
			throw new SystemException(SystemError.DF_EMPTY_LINE, lineNumber);
		}

		if (!isFixedLength())
			return mapLineDelitemed(line, separator, variables, lineNumber);
		else
			return mapLineFixedLength(line, variables, fixedLength, lineNumber);

	}

	/**
	 * Process a delimited line and returns a new instance of an import line with
	 * the parsed values
	 * 
	 * @param line      line to process
	 * @param delimiter delimiter to use
	 * @param variables variables based on which the file should be parsed
	 * @return string entries forming the line
	 * @throws SystemException
	 */
	private FormatLineParseResult mapLineDelitemed(String line, String delimiter, List<FormatVar> variables,
			int lineNumber) throws SystemException {

		String lineDelim = line;

		if (!StringUtils.isEmpty(endOfLine)) {
			while (appendSeparator && lineDelim.contains(separator + endOfLine)) {
				lineDelim = lineDelim.replace(delimiter + endOfLine, delimiter + " " + endOfLine);
			}
			if (StringUtils.isNotEmpty(endOfLine) && !(endOfLine.equals("\n") || endOfLine.equals("\r\n"))) {
				lineDelim = lineDelim.substring(0, lineDelim.length() - endOfLine.length());

			}
		}

		if (appendSeparator) {
			lineDelim += delimiter;
		}

		while (lineDelim.contains(separator + separator)) {
			lineDelim = lineDelim.replace(delimiter + delimiter, delimiter + " " + delimiter);
		}

		while (lineDelim.contains(separator + separator)) {
			lineDelim = lineDelim.replace(delimiter + delimiter, delimiter + " " + delimiter);
		}

		String delim = Utils.escapeAllNonAlphaNum(delimiter);

		String[] parts = lineDelim.split(delim);

		if (parts.length != variables.size()) {
			throw new SystemException(SystemError.DF_INVALID_LINE_PARTS, variables.size(), parts.length);
		}

		return verifyMapVariables(variables, parts, lineNumber);

	}

	/**
	 * Processes a fixed length line and returns a new instance of an import line
	 * with the parsed values
	 * 
	 * @param line        line to parse
	 * @param variables   variables based on which the line will be parsed
	 * @param fixedLength fixed length of the record
	 * @return string array of the parsed line based on the specified variables
	 * @throws SystemException
	 */
	private FormatLineParseResult mapLineFixedLength(String line, List<FormatVar> variables, int fixedLength,
			int lineNumber) throws SystemException {

		int variableLength = fixedLength;

		if (!StringUtils.isEmpty(endOfLine) && line.endsWith(endOfLine)) {
			line = line.substring(0, line.length() - endOfLine.length());
		}

		if (!skipLengthCheck && ((line.getBytes().length != variableLength && checkLengthInBytes)
				|| (!checkLengthInBytes && line.length() != variableLength))) {
			throw new SystemException(SystemError.DF_INVALID_LINE_LENGTH, variableLength,
					checkLengthInBytes ? line.getBytes().length : line.length());
		}

		int currentLength = 0;

		FormatLineParseResult result = new FormatLineParseResult();
		result.setLineNumber(lineNumber);
		int dataLength = 0;
		if (variables != null && !variables.isEmpty()) {

			for (FormatVar v : variables) {
				String part = null;
				if (StringUtils.isBlank(v.getLengthFormat())) {
					dataLength = v.getMaxLen();
				} else {
					dataLength = Integer
							.parseInt(line.substring(currentLength, currentLength + v.getLengthFormat().length()));
					currentLength += v.getLengthFormat().length();
					formatFile.setSkipLengthCheck(true);
				}

				if (checkLengthInBytes) {
					part = new String(
							Arrays.copyOfRange(line.getBytes(), currentLength, currentLength + v.getMaxLen()));
				} else {
					part = line.substring(currentLength, currentLength + v.getMaxLen());
				}

				if (v.isAllowTrim())
					part = part.trim();

				currentLength += dataLength;
				result.addParseResultEntry(v, getObjectValue(part, v));
			}
		}
		return result;
	}

	/**
	 * Verifies the variables passed to it
	 * 
	 * @param variables  variables used to verify the values
	 * @param parts      variables values to be verified
	 * @param lineNumber line number for which the variable values are being
	 *                   verified
	 * 
	 * @throws SystemException
	 */
	private FormatLineParseResult verifyMapVariables(List<FormatVar> variables, String[] parts, Integer lineNumber)
			throws SystemException {

		int i = 0;

		FormatLineParseResult result = new FormatLineParseResult();

		result.setLineNumber(lineNumber);

		for (FormatVar formatVar : variables) {

			result.addParseResultEntry(formatVar, getObjectValue(parts[i], formatVar));
			i++;

		}

		return result;
	}

	/**
	 * Gets an object value
	 * 
	 * @param part      part to get the value
	 * @param formatVar format variable of the part
	 * @return Object value after parsing
	 * @throws SystemException
	 */
	private Object getObjectValue(String part, FormatVar formatVar) throws SystemException {

		Object object = null;

		if (debugMode) {
			log.info("Verifying field [" + part + "]");
			DebugUtils.dumpObject(formatVar);
		}

		if (formatVar.getType() == VT.Date) {
			Date defaultValue = null;
			if (formatVar.getDefaultValue() != null
					&& StringUtils.isNotBlank(String.valueOf(formatVar.getDefaultValue()))) {
				try {
					// Makes sure the date matches exactly the format
					formatVar.getSdf().setLenient(false);
					defaultValue = formatVar.getSdf().parse(String.valueOf(formatVar.getDefaultValue()));
				} catch (ParseException e) {
					// this should never happen as the default date should
					// have been verified before when parsing the layout
					// file
				}

			}

			// Consider a 0 date as null
			if (part != null && StringUtils.isNotBlank(String.valueOf(part)) && StringUtils.isNumeric(part)
					&& 0 == Long.valueOf(String.valueOf(part)).longValue()) {
				part = null;
			}
			object = Utils.verifyDate(part, formatVar.isMandatory(), formatVar.getName(), defaultValue,
					formatVar.getSdf());
		} else {
			Class<?> type = String.class;
			if (formatVar.getType() == VT.Float)
				type = BigDecimal.class;
			else if (formatVar.getType() == VT.Integer)
				type = Integer.class;
			else if (formatVar.getType() == VT.Long)
				type = Long.class;
			else if (formatVar.getType() == VT.Short)
				type = Short.class;

			if (part != null && (formatVar.getType() == VT.Float || formatVar.getType() == VT.Long
					|| formatVar.getType() == VT.Short || formatVar.getType() == VT.Integer))
				part = String.valueOf(part).trim();

			part = Utils.verifyMinMaxMandatory(part, formatVar.isAllowPad() ? null : formatVar.getMinLen(),
					formatVar.isAllowTrim() ? null : formatVar.getMaxLen(),
					null == formatVar.getDefaultValue() ? formatVar.isMandatory() : false, formatVar.getName(),
					null == formatVar.getDefaultValue() ? null : String.valueOf(formatVar.getDefaultValue()),
					formatVar.getAllowedValues());

			int minLength = formatVar.getMinLen() == null ? 0 : formatVar.getMinLen();

			int maxLength = formatVar.getMaxLen() == null ? 0 : formatVar.getMaxLen();

			if (formatVar.isAllToUpper() && StringUtils.isNotBlank(part))
				part = part.toUpperCase();

			if (formatVar.isAllToLower() && StringUtils.isNotBlank(part))
				part = part.toLowerCase();

			String substitution = DataFormatUtils.verifyAjustSubstitution(part, minLength, maxLength,
					formatVar.isAllowTrim(), formatVar.isTrimRight(), formatVar.isAllowPad(),
					formatVar.getPaddingChar().charAt(0), formatVar.isPadRight(), !formatVar.isMandatory(),
					formatVar.getName(), formatVar.getName(), formatVar.getType(), formatVar.getAllowedSeperator(),
					formatVar.getPatternPart(), null, null);

			if (StringUtils.isNotBlank(substitution))
				object = Utils.validateDataType(substitution, formatVar.getName(), type);

			if (formatVar.getType() == VT.Float && formatVar.getDecimalNb() != null
					&& formatVar.getDecimalNb().intValue() > 0
					&& StringUtils.isBlank(formatVar.getCurrencyVariableName())) {
				object = ((BigDecimal) object).divide(
						BigDecimal.valueOf(Math.pow(10, formatVar.getDecimalNb().longValue())), 10,
						RoundingMode.UNNECESSARY);
			}

		}

		return object;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * =================================================================
	 * Initialization function
	 * =================================================================
	 */

	/**
	 * Constructor that parses a configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @param fileVersion file version being imported
	 * @throws Exception
	 */
	public FormatLineParser(String description, String fileName, String parsedXmlFile, boolean debugMode,
			Short fileVersion) throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		formatFile = parser.parseFile(description, fileName, parsedXmlFile, fileVersion);

		this.initParser(fileName, description, formatFile.isFixedLength(), formatFile.getDelimiter(),
				formatFile.getVars(), formatFile.getHeaderVars(), formatFile.getFooterVars(),
				formatFile.getCheckLengthInBytes(), formatFile.getAppendSeperator(), debugMode,
				formatFile.getTableName(), null, null, formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());
	}

	/**
	 * Constructor that parses a configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @throws Exception
	 */
	public FormatLineParser(String description, String fileName, String parsedXmlFile, boolean debugMode)
			throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		formatFile = parser.parseFile(description, fileName, parsedXmlFile, null);

		this.initParser(fileName, description, formatFile.isFixedLength(), formatFile.getDelimiter(),
				formatFile.getVars(), formatFile.getHeaderVars(), formatFile.getFooterVars(),
				formatFile.getCheckLengthInBytes(), formatFile.getAppendSeperator(), debugMode,
				formatFile.getTableName(), null, null, formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());
	}

	/**
	 * Constructor that parses a configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @param fileVersion file version
	 * @throws Exception
	 */
	public FormatLineParser(String description, String configFile, boolean debugMode, Short fileVersion)
			throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		formatFile = parser.parseFile(description, configFile, fileVersion);

		this.initParser(configFile, description, formatFile.isFixedLength(), formatFile.getDelimiter(),
				formatFile.getVars(), formatFile.getHeaderVars(), formatFile.getFooterVars(),
				formatFile.getCheckLengthInBytes(), formatFile.getAppendSeperator(), debugMode,
				formatFile.getTableName(), null, null, formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());
	}

	/**
	 * Constructor that parses a configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @throws Exception
	 */
	public FormatLineParser(String description, String configFile, boolean debugMode) throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		formatFile = parser.parseFile(description, configFile, null);

		this.initParser(configFile, description, formatFile.isFixedLength(), formatFile.getDelimiter(),
				formatFile.getVars(), formatFile.getHeaderVars(), formatFile.getFooterVars(),
				formatFile.getCheckLengthInBytes(), formatFile.getAppendSeperator(), debugMode,
				formatFile.getTableName(), null, null, formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());
	}

	/**
	 * Constructor that verifies a parsed configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @param fileVersion file version
	 * @throws Exception
	 */
	public FormatLineParser(String description, String source, FormatFile config, boolean debugMode, Short fileVersion)
			throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		this.formatFile = config;
		parser.verifyImportFile(formatFile, source, description, fileVersion);

		initParser(source, description, formatFile.isFixedLength(), formatFile.getDelimiter(), formatFile.getVars(),
				formatFile.getHeaderVars(), formatFile.getFooterVars(), formatFile.getCheckLengthInBytes(),
				formatFile.getAppendSeperator(), debugMode, formatFile.getTableName(), null, null,
				formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());

	}

	/**
	 * Constructor that verifies a parsed configuration file and works on it
	 * 
	 * @param description description of the import we are parsing the file for
	 * @param configFile  configuration file name to parse
	 * @param debugMode   specifies if we're in debug mode
	 * @throws Exception
	 */
	public FormatLineParser(String description, String source, FormatFile config, boolean debugMode) throws Exception {

		log.debug("Initializing custom line parser");
		FormatFileParser parser = new FormatFileParser();
		this.formatFile = config;
		parser.verifyImportFile(formatFile, source, description, null);

		initParser(source, description, formatFile.isFixedLength(), formatFile.getDelimiter(), formatFile.getVars(),
				formatFile.getHeaderVars(), formatFile.getFooterVars(), formatFile.getCheckLengthInBytes(),
				formatFile.getAppendSeperator(), debugMode, formatFile.getTableName(), null, null,
				formatFile.getEndOfLine(),
				formatFile.getImportLine() != null ? formatFile.getImportLine().getUniqueKey() : null,
				formatFile.isSkipLengthCheck());

	}

	/**
	 * Function to initialize the parser
	 * 
	 * @param source             description of the source of the configuration
	 * @param description        description of the import processing
	 * @param isFixedLength      specifies if the file is fixed length or delimited
	 * @param delimiter          delimiter to be used for the file
	 * @param variables          variables of each line
	 * @param headerVariables    header variables
	 * @param footerVariables    footer variables
	 * @param checkLengthInBytes specifies if the length of the fields should be
	 *                           checked in bytes
	 * @param appendSeparator    specifies if a separator should be appended to the
	 *                           end of line before parsing
	 * @param debugMode          specifies if we are in debug mode (a lot of details
	 *                           will be displayed in this case)
	 * @param tableName          table name that the insert will be done in
	 * @param headerTableName    header table name to insert header data in
	 * @param footerTableName    footer table name to insert footer data in
	 * @param endOfLine          end of line used
	 * @param uniqueKeyList      unique key list in case any to validate duplicates
	 * @throws SystemException
	 */
	private void initParser(String source, String description, boolean isFixedLength, String delimiter,
			List<FormatVar> variables, List<FormatVar> headerVariables, List<FormatVar> footerVariables,
			boolean checkLengthInBytes, Boolean appendSeparator, boolean debugMode, String tableName,
			String headerTableName, String footerTableName, String endOfLine, List<UniqueKey> uniqueKeyList,
			boolean skipLengthCheck) throws SystemException {

		log.debug("Initializing custom line parser");

		if (appendSeparator == null)
			appendSeparator = false;

		this.fixedLength = isFixedLength;
		this.separator = delimiter;
		this.variables = variables;
		this.headerVariables = headerVariables;
		this.footerVariables = footerVariables;
		this.checkLengthInBytes = checkLengthInBytes;
		this.skipLengthCheck = skipLengthCheck;

		if (!this.fixedLength && appendSeparator) {
			this.appendSeparator = true;

		} else if (!this.fixedLength) {
			this.appendSeparator = appendSeparator;
		}

		if (Utils.emptyList(uniqueKeyList)) {
			this.uniqueKeys = uniqueKeyList;
		}
		this.debugMode = debugMode;

		this.migrateDb = true;
		this.tableName = tableName;
		this.headerTableName = headerTableName;
		this.footerTableName = footerTableName;

		if (!StringUtils.isEmpty(endOfLine)) {
			this.endOfLine = endOfLine;
		}

		if (Utils.emptyList(variables)) {
			String error = "Trying to create a parser with no variables sepcified";
			log.error(error);
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, source, description, "No variables defined");
		}

		Collections.sort(variables);

		if (!Utils.emptyList(footerVariables))
			Collections.sort(footerVariables);

		if (!Utils.emptyList(headerVariables))
			Collections.sort(headerVariables);

		if (isFixedLength()) {
			if (!Utils.emptyList(variables)) {
				fixedLengthVariablesLength = 0;

				for (FormatVar v : variables) {
					if (v.getOrder() >= 0)
						fixedLengthVariablesLength += v.getMaxLen();
				}
				log.debug("Fixed variables length [" + fixedLengthVariablesLength + "]");
			}

			if (!Utils.emptyList(footerVariables)) {
				fixedLengthFooterVariablesLength = 0;
				for (FormatVar v : footerVariables) {
					if (v.getOrder() >= 0)
						fixedLengthFooterVariablesLength += v.getMaxLen();
				}
				log.debug("Fixed variables footer length [" + fixedLengthFooterVariablesLength + "]");
			}

			if (!Utils.emptyList(headerVariables)) {
				fixedLengthHeaderVariablesLength = 0;
				for (FormatVar v : headerVariables) {
					if (v.getOrder() >= 0)
						fixedLengthHeaderVariablesLength += v.getMaxLen();
				}
				log.debug("Fixed variables header length [" + fixedLengthHeaderVariablesLength + "]");
			}
		}

		log.debug(variables.size() + " Variables in file");

	}

	public boolean isFixedLength() {
		return fixedLength;
	}

	public void setFixedLength(boolean fixedLength) {
		this.fixedLength = fixedLength;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public List<FormatVar> getVariables() {
		return variables;
	}

	public void setVariables(List<FormatVar> variables) {
		this.variables = variables;
	}

	public List<FormatVar> getHeaderVariables() {
		return headerVariables;
	}

	public void setHeaderVariables(List<FormatVar> headerVariables) {
		this.headerVariables = headerVariables;
	}

	public List<FormatVar> getFooterVariables() {
		return footerVariables;
	}

	public void setFooterVariables(List<FormatVar> footerVariables) {
		this.footerVariables = footerVariables;
	}

	public List<UniqueKey> getUniqueKeys() {
		return uniqueKeys;
	}

	public void setUniqueKeys(List<UniqueKey> uniqueKeys) {
		this.uniqueKeys = uniqueKeys;
	}

	public boolean isAppendSeparator() {
		return appendSeparator;
	}

	public void setAppendSeparator(boolean appendSeparator) {
		this.appendSeparator = appendSeparator;
	}

	public boolean isMigrateDb() {
		return migrateDb;
	}

	public void setMigrateDb(boolean migrateDb) {
		this.migrateDb = migrateDb;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getHeaderTableName() {
		return headerTableName;
	}

	public void setHeaderTableName(String headerTableName) {
		this.headerTableName = headerTableName;
	}

	public String getFooterTableName() {
		return footerTableName;
	}

	public void setFooterTableName(String footerTableName) {
		this.footerTableName = footerTableName;
	}

	public String getEndOfLine() {
		return endOfLine;
	}

	public void setEndOfLine(String endOfLine) {
		this.endOfLine = endOfLine;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public int getFixedLengthVariablesLength() {
		return fixedLengthVariablesLength;
	}

	public int getFixedLengthHeaderVariablesLength() {
		return fixedLengthHeaderVariablesLength;
	}

	public int getFixedLengthFooterVariablesLength() {
		return fixedLengthFooterVariablesLength;
	}

	public FormatFile getFormatFile() {
		return formatFile;
	}

}
