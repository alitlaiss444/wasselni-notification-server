package com.wasselni.common.utils.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.common.utils.Utils;
import com.wasselni.common.utils.format.domain.FormatVar;
import com.wasselni.common.utils.format.domain.FormatVar.VT;
import com.wasselni.common.utils.format.xml.FormatFile;
import com.wasselni.common.utils.format.xml.Variable;

/**
 * Utility function to parse a format file xml configuration
 */
public class FormatFileParser {

	private static final Log log = LogFactory.getLog(FormatFileParser.class);

	private static final HashMap<String, VT> supportedVariableTypes = new HashMap<String, FormatVar.VT>() {
		private static final long serialVersionUID = 1L;

		{
			put("Short", VT.Short);
			put("String", VT.String);
			put("Long", VT.Long);
			put("Integer", VT.Integer);
			put("Date", VT.Date);
			put("Float", VT.Float);
			put("Pan", VT.Pan);
			put("BLOB", VT.BLOB);
			put("CLOB", VT.CLOB);
			put("COBOL", VT.COBOL);

		}
	};

	/**
	 * Parses a configuration file and returns the object required
	 * 
	 * @param description description of the file being parsed used for debugging
	 * @param fileName    file name to read and parse
	 * @param fileVersion file version
	 * @return object holding the information required for a format file
	 * @throws SystemException if the file is not found or not valid
	 */
	public FormatFile parseFile(String description, String fileName, Short fileVersion) throws SystemException {

		if (StringUtils.isBlank(fileName)) {
			throw new SystemException(SystemError.VE_MISSING_MANDATORY, description + " FileName");
		}

		log.debug("Parsing configuraion from file [" + fileName + "]");

		String xmlFileContent = readFile(description, fileName);

		return parseFile(description, fileName, xmlFileContent, fileVersion);

	}

	/**
	 * Parses a configuration file and returns the object required
	 * 
	 * @param description description of the file being parsed used for debugging
	 * @param fileName    file name to read and parse
	 * @param fileVersion file version
	 * @return object holding the information required for a format file
	 * @throws SystemException if the file is not found or not valid
	 */
	public FormatFile parseFile(String description, String fileName) throws SystemException {

		return parseFile(description, fileName, null);

	}

	/**
	 * Parses a file with the content already loaded in memory
	 * 
	 * @param description    description of the file
	 * @param fileName       file name
	 * @param xmlFileContent XML content of the file
	 * @param fileVersion    file version
	 * @return parsed {@link FormatFile}
	 * @throws SystemException
	 */
	public FormatFile parseFile(String description, String fileName, String xmlFileContent, Short fileVersion)
			throws SystemException {
		FormatFile formatFile = new FormatFile();

		formatFile = FormatFileMarshaller.getConfigObject(xmlFileContent);
		if (formatFile == null) {
			log.error("Failed parsing configuration file");
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, fileName, description, "Failed parsing file");
		}

		verifyImportFile(formatFile, fileName, description, fileVersion);

		return formatFile;
	}

	/**
	 * Verifies the data of the configuration in the input file
	 * 
	 * @param formatFile  parsed file to be verified
	 * @param fileName    file name of the original file
	 * @param desc        description of the file used for debugging
	 * @param fileVersion file version
	 * @throws SystemException file is invalid
	 */
	public void verifyImportFile(FormatFile formatFile, String fileName, String desc, Short fileVersion)
			throws SystemException {

		log.debug("Verifying configuration file [" + fileName + "] for [" + desc + "]");

		verifyFileTypeAndDelimiter(formatFile, fileName, desc);

		if (formatFile.getImportLine() == null)
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, fileName, desc, "Import Line Not Defined");

		if (Utils.emptyList(formatFile.getImportLine().getVariable()))
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, fileName, desc,
					"No Variables defined in file");

		List<FormatVar> vars = new ArrayList<FormatVar>();
		for (Variable v : formatFile.getImportLine().getVariable()) {

			if (v.getVersionNumber() == null) {
				v.setVersionNumber((short) 1);
			}

			if (fileVersion == null || fileVersion.shortValue() <= v.getVersionNumber().shortValue()) {
				vars.add(verifyLineVariable(v, fileName, desc, vars, formatFile.isFixedLength(),
						formatFile.isExcelOutput()));
			}
		}

		formatFile.setVars(vars);
		if (null != formatFile.getHeader()) {
			vars = new ArrayList<FormatVar>();
			for (Variable v : formatFile.getHeader().getVariable()) {

				if (v.getVersionNumber() == null) {
					v.setVersionNumber((short) 1);
				}
				if (fileVersion == null || fileVersion.shortValue() <= v.getVersionNumber().shortValue()) {
					vars.add(verifyLineVariable(v, fileName, desc, vars, formatFile.isFixedLength(),
							formatFile.isExcelOutput()));
				}

			}

			formatFile.setHeaderVars(vars);
		}

		if (null != formatFile.getFooter()) {
			vars = new ArrayList<FormatVar>();
			for (Variable v : formatFile.getFooter().getVariable()) {

				if (v.getVersionNumber() == null) {
					v.setVersionNumber((short) 1);
				}

				if (fileVersion == null || fileVersion.shortValue() <= v.getVersionNumber().shortValue()) {
					vars.add(verifyLineVariable(v, fileName, desc, vars, formatFile.isFixedLength(),
							formatFile.isExcelOutput()));
				}
			}
			formatFile.setFooterVars(vars);
		}
	}

	/**
	 * Verifies the file type and delimiter
	 * 
	 * @param formatFile  format file to verify
	 * @param fileName    format file name
	 * @param description description of the file
	 * @throws SystemException
	 */
	private void verifyFileTypeAndDelimiter(FormatFile formatFile, String fileName, String description)
			throws SystemException {

		// Check type and delimiter
		if (StringUtils.isBlank(formatFile.getType())) {
			throw new SystemException(SystemError.DF_PARSER_MISSING_MANDATORY_TAG, fileName, description, "Type");
		}

		if (!formatFile.getType().equals(FormatFile.fileTypeDelimited)
				&& !formatFile.getType().equals(FormatFile.fileTypeFixedLength)
				&& !formatFile.getType().equals(FormatFile.fileTypeExcel)) {

			throw new SystemException(SystemError.DF_PARSER_INVALID_TAG_VALUE, fileName, description,
					formatFile.getType(), "Type", FormatFile.fileTypeDelimited + " OR " + FormatFile.fileTypeFixedLength
							+ " OR " + FormatFile.fileTypeExcel);
		}

		if (!formatFile.isFixedLength() && !formatFile.isExcelOutput()
				&& StringUtils.isEmpty(formatFile.getDelimiter())) {
			throw new SystemException(SystemError.DF_PARSER_MISSING_MANDATORY_TAG, fileName, description, "Delimiter");
		}

		if (!formatFile.isFixedLength() && StringUtils.isEmpty(formatFile.getEndOfLine())) {
			formatFile.setEndOfLine("");
		}
		if (null == formatFile.isMigrateDb()) {
			formatFile.setMigrateDb(false);
		}
		if (formatFile.isMigrateDb() && StringUtils.isEmpty(formatFile.getTableName())) {
			throw new SystemException(SystemError.DF_PARSER_MISSING_MANDATORY_TAG, fileName, description, "Table Name");
		}

		if (null == formatFile.getCheckLengthInBytes())
			formatFile.setCheckLengthInBytes(false);
	}

	/**
	 * Verifies a single variable
	 * 
	 * @param variable      variable to validate
	 * @param fileName      file name from which the configuration was loaded
	 * @param description   description of the configuration file
	 * @param vars          variables read so far
	 * @param isFixedLength specifies if the file is fixed length
	 * @throws SystemException
	 */
	private FormatVar verifyLineVariable(Variable variable, String fileName, String description, List<FormatVar> vars,
			boolean isFixedLength, boolean isExcel) throws SystemException {

		String name = variable.getName();
		String patternPart = variable.getPatternPart();
		String fieldName = "";
		Integer decimalNb = 0;

		if (StringUtils.isBlank(name)) {
			throw new SystemException(SystemError.DF_PARSER_MISSING_MANDATORY_TAG, fileName, description,
					"Variable Name");
		}

		VT type = VT.String;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		DecimalFormat df = null;
		List<String> patternSeperator = null;
		if (StringUtils.isNotBlank(patternPart)) {
			patternSeperator = new ArrayList<String>();
			if (variable.getAllowedSeperator() != null && !Utils.emptyList(variable.getAllowedSeperator().getValue())) {
				for (String s : variable.getAllowedSeperator().getValue()) {
					patternSeperator.add(s);
				}
			}
		}
		if (StringUtils.isNotBlank(variable.getType()) && null == supportedVariableTypes.get(variable.getType())) {
			throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG, fileName, description, variable.getType(),
					"Type", variable.getName(), "[Short] or [String] or [Long] or [Integer] or [Date] or [Float]");
		} else if (StringUtils.isNotBlank(variable.getType())) {
			type = supportedVariableTypes.get(variable.getType());
		}

		if (type == VT.Date && StringUtils.isNotBlank(variable.getDateFormat())) {
			try {
				sdf = new SimpleDateFormat(variable.getDateFormat());
				sdf.format(new Date());
			} catch (Exception e) {
				throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description,
						variable.getDateFormat(), "DateFormat", variable.getName(),
						"Date Format should be a valid java SimpleDateFormat pattern");
			}
		} else if (type == VT.Float && StringUtils.isNotBlank(variable.getDecimalFormat())) {
			try {
				df = new DecimalFormat(variable.getDecimalFormat());
			} catch (Exception e) {
				throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description,
						variable.getDecimalFormat(), "DecimalFormat", variable.getName(),
						"Decimal Format should be a valid java DecimalFormat pattern");
			}
		}

		Integer minLen = verifyNumericValue(fileName, description, variable.getMinLen(), "MinLen", variable.getName());
		Integer maxLen = verifyNumericValue(fileName, description, variable.getMaxLen(), "MaxLen", variable.getName());
		if (maxLen == null && type == VT.Date) {
			maxLen = sdf.toPattern().length();
		}
		if (isFixedLength && maxLen == null)
			throw new SystemException(SystemError.VE_GENERIC,
					"Missing Maximum Length for Variable [" + variable.getName() + "] in Fixed Length File Format");

		boolean mandatory = getBooleanParam(variable, fieldName, description, variable.getMandatory(), "Mandatory",
				false);

		int order = 0;
		if (StringUtils.isBlank(variable.getOrder()))
			throw new SystemException(SystemError.DF_PARSER_MISSING_MANDATORY_TAG_FOR_VAR, fileName, description,
					"Order", variable.getName());
		order = verifyNumericValueAllowAll(fileName, description, variable.getOrder(), "Order", variable.getName());
		if (null != variable.getDecimalNb()) {
			decimalNb = variable.getDecimalNb();
		} else {
			decimalNb = 0;
		}

		fieldName = variable.getFieldId();

		if (Utils.emptyList(vars)) {
			for (FormatVar var : vars) {
				if (var.getOrder() == order) {
					throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE,
							"Both variable [" + variable.getName() + "] and [" + var.getDefaultValue()
									+ "] have the same order [" + variable.getOrder() + "]");
				}
				if (var.getFieldName() == fieldName) {
					throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE,
							"Both variable [" + variable.getName() + "] and [" + var.getDefaultValue()
									+ "] ar linked to the same field in the database [" + variable.getFieldId() + "]");
				}
			}
		}

		boolean keepEmpty = getBooleanParam(variable, fileName, description, variable.getKeepEmpty(), "KeepEmpty",
				true);

		Object defaultValue = verifytDataTypeAndGetValue(variable.getDefaultValue(), mandatory, type, sdf, fileName,
				description, variable.getName(), "DefaultValue", isExcel, keepEmpty);

		List<Object> allowedValues = new ArrayList<Object>();
		if (variable.getAllowedValues() != null && !Utils.emptyList(variable.getAllowedValues().getValue())) {
			for (String s : variable.getAllowedValues().getValue()) {
				allowedValues.add(verifytDataTypeAndGetValue(s, true, type, sdf, fileName, description,
						variable.getName(), "AllowedValues", isExcel, keepEmpty));
			}
		}

		boolean allowTrim = getBooleanParam(variable, fileName, description, variable.getAllowTrim(), "AllowTrim",
				false);
		boolean trimRight = getBooleanParam(variable, fileName, description, variable.getTrimRight(), "TrimRight",
				false);
		boolean allowPad = getBooleanParam(variable, fileName, description, variable.getAllowPad(), "AllowPad", false);
		boolean allToUpper = getBooleanParam(variable, fileName, description, variable.getAllToUpper(), description,
				false);
		boolean allToLower = getBooleanParam(variable, fileName, description, variable.getAllToLower(), description,
				false);
		boolean currencyAlpha = getBooleanParam(variable, fileName, description, variable.getCurrencyAlpha(),
				description, false);

		boolean maskMap = getBooleanParam(variable, fileName, description, variable.getMaskPan(), "MaskPan", false);

		boolean trimAllSpaces = StringUtils.isNotBlank(variable.getTrimSpaces())
				&& variable.getTrimSpaces().equalsIgnoreCase("ALL");
		boolean trimSpaces = false;
		if (!trimAllSpaces)
			trimSpaces = getBooleanParam(variable, fileName, description, variable.getTrimSpaces(), description, false);

		String paddingChar = StringUtils.isBlank(variable.getPaddingChar()) ? " " : variable.getPaddingChar();
		if (paddingChar.length() > 1) {
			throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description,
					variable.getPaddingChar(), "Padding Char", variable.getName(), "Length Should be exactly 1");
		}

		boolean padRight = getBooleanParam(variable, fileName, paddingChar, variable.getPadRight(), "PadRight", false);

		FormatVar newVar = new FormatVar(name, variable.getMapId(), type, minLen, maxLen, mandatory, order,
				defaultValue, allowedValues, sdf, allowTrim, trimRight, allowPad, paddingChar, padRight, fieldName,
				decimalNb, allToUpper, allToLower, patternPart, patternSeperator, trimSpaces,
				variable.getCurrencyVariable(), currencyAlpha, variable.getCurrencyVerificationMethod(),
				variable.getCountryVerificationMethod(),
				variable.getVersionNumber() == null ? (short) 1 : variable.getVersionNumber(), trimAllSpaces, keepEmpty,
				variable.getLengthFormat(), variable.getPrefix(), variable.getSuffix(), df, maskMap);

		return newVar;
	}

	/**
	 * Gets and verifies a boolean parameter
	 * 
	 * @param variable            variable to verify
	 * @param fileName            file name having the variable
	 * @param description         description of the file having this variable
	 * @param value               value of the variable
	 * @param variableDescription description of the variable
	 * @param defaultValue        default value to use in case the value is empty
	 * @return
	 * @throws SystemException
	 */
	private boolean getBooleanParam(Variable variable, String fileName, String description, String value,
			String variableDescription, boolean defaultValue) throws SystemException {

		boolean ret = defaultValue;

		if (StringUtils.isNotBlank(value) && !value.equals("true") && !value.equals("false")) {
			throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description, value,
					variableDescription, variable.getName(), "[true] or [false]");
		} else if (StringUtils.isNotBlank(value)) {
			ret = Boolean.valueOf(value);
		}

		return ret;
	}

	/**
	 * Verifies the default object and gets the value
	 * 
	 * @param value       to verify the data type for
	 * @param mandatory   specifies if the field is mandatory
	 * @param type        specifies the type of the field to verify
	 * @param sdf         simple date format to use for the verification in case of
	 *                    a date variable
	 * @param fileName    file name of the original format file
	 * @param description description of the format file
	 * @param varName     variable name being verified
	 * @param tagName     tag name of the variable being verified
	 * @param isExcel     specifies if the variable is for an excel output
	 * @param keepEmpty   if true, do not initiate object in case it's null.
	 * @return output value of the object with the required type
	 * @throws SystemException
	 */
	private Object verifytDataTypeAndGetValue(Object object, boolean mandatory, VT type, SimpleDateFormat sdf,
			String fileName, String description, String varName, String tagName, boolean isExcel, boolean keepEmpty)
			throws SystemException {

		if (object != null) {

			if (type == VT.Date) {
				try {
					Utils.verifyDate(String.valueOf(object), true, "Date", null, sdf);
				} catch (Exception e) {
					throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description,
							object, tagName, varName, "Invalid date");
				}

			} else {
				Class<?> t = String.class;
				if (type == VT.Float)
					t = BigDecimal.class;
				else if (type == VT.Integer)
					t = Integer.class;
				else if (type == VT.Long)
					t = Long.class;
				else if (type == VT.Short)
					t = Short.class;
				try {
					Utils.validateDataType(String.valueOf(object), varName, t);
				} catch (Exception e) {
					throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, description,
							object, tagName, varName, "Invalid [" + type + "]");
				}
			}
		} else if (!mandatory && !isExcel) {

			if (keepEmpty)
				return object;

			switch (type) {
			case Date:
				object = new Date();
				break;
			case Integer:
				object = new Integer(0);
				break;
			case Long:
				object = new Long(0);
				break;
			case Float:
				object = BigDecimal.ZERO;
				break;
			case Short:
				object = (short) 0;
				break;
			case String:
			case BLOB:
			case CLOB:
			case COBOL:
				object = " ";
				break;

			default:
				break;
			}
		}

		return object;
	}

	/**
	 * Verifies a numeric value and returns it parsed one as integer
	 * 
	 * @param fileName      format file name holding the value
	 * @param value         value to verify
	 * @param tagName       tag name holding the value
	 * @param parentTagName parent tag name holding the value
	 * @return numeric value of the passed string value
	 * @throws SystemException
	 */
	private Integer verifyNumericValue(String fileName, String d, String value, String tagName, String parentTagName)
			throws SystemException {
		Integer ret = null;
		if (StringUtils.isNotBlank(value)) {
			try {
				ret = Integer.parseInt(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, d, value, tagName,
						parentTagName, "Value should be numeric");
			}
		}
		if (ret != null && ret == -1)
			ret = null;

		return ret;
	}

	/**
	 * Verifies a numeric value and returns it parsed one as integer, allow -1 value
	 * 
	 * @param fileName      format file name holding the value
	 * @param value         value to verify
	 * @param tagName       tag name holding the value
	 * @param parentTagName parent tag name holding the value
	 * @return numeric value of the passed string value
	 * @throws SystemException
	 */
	private Integer verifyNumericValueAllowAll(String fileName, String d, String value, String tagName,
			String parentTagName) throws SystemException {
		Integer ret = null;
		if (StringUtils.isNotBlank(value)) {
			try {
				ret = Integer.parseInt(value);
			} catch (Exception e) {
				throw new SystemException(SystemError.DF_PARSER_INVALID_VAR_TAG_VALUE, fileName, d, value, tagName,
						parentTagName, "Value should be numeric");
			}
		}

		return ret;
	}

	/**
	 * Reads the file contents and returns them as string
	 * 
	 * @param desc description of the file being read
	 * @param file file name to read and get data as string
	 * @return
	 * @throws SystemException
	 */
	private String readFile(String desc, String fileName) throws SystemException {

		File file = new File(fileName);

		String out = "";

		if (!file.exists()) {
			throw new SystemException(SystemError.DF_PARSER_CONFIG_FILE_NOT_FOUND, file.getAbsolutePath(), desc);
		}

		if (file.isDirectory()) {
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, file.getAbsolutePath(), desc,
					"Specified resource is a directory");
		}

		if (!file.canRead()) {
			throw new SystemException(SystemError.DF_INVALID_CONFIG_FILE, file.getAbsolutePath(), desc,
					"No permission to read file");
		}

		BufferedReader reader;
		try {
			char[] buf = new char[1024];
			int numRead = 0;
			reader = new BufferedReader(new FileReader(file));
			while ((numRead = reader.read(buf)) != -1) {
				out += String.valueOf(buf, 0, numRead);
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			throw new SystemException(SystemError.DF_PARSER_CONFIG_FILE_NOT_FOUND, file.getAbsolutePath(), desc);
		} catch (IOException e) {
			log.error("Failed reading file!", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					"Failed loading config file [" + file.getAbsolutePath());
		}

		return out;
	}
}
