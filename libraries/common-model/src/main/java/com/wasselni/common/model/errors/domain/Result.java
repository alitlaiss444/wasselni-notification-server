package com.wasselni.common.model.errors.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wasselni.common.model.errors.ErrorEnum;
import com.wasselni.common.model.errors.constants.ErrorType;
import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.utils.DateUtils;
import com.wasselni.common.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(Result.class);
	private static Map<String, ResourceBundle> translations = new HashMap<String, ResourceBundle>();

	// Code identifying the result
	private String code;
	// Type of error
	private ErrorType type;
	// Description of the result
	private String message;
	// Data object holding return results needed by the calling function
	private Object data;
	// Savepoint to rollback changes to in case we don't want to rollback the
	// whole changes
	@JsonIgnore
	private Savepoint savepoint;
	// List of warnings if any
	private List<String> warnings;
	// Type of class
	@JsonIgnore
	private Class<?> clazz;
	private String clazzName;
	// Processing time in ms
	private Long processingTime;
	// Message arguments (used for message localization in case the messages are
	// Override at client level)
	private List<String> messageArgs;

	public Result() {
		super();
	}

	/**
	 * Creates a new instance of the result object with the specified code, message,
	 * type and savepoint to rollback to if any
	 * 
	 * @param code
	 * @param message
	 * @param type
	 * @param savepoint
	 */
	private Result(String code, String message, ErrorType type, Savepoint savepoint, List<String> messageArgs) {
		super();
		this.code = code;
		this.type = type;
		this.message = message;
		this.savepoint = savepoint;
		this.messageArgs = messageArgs;
	}

	/**
	 * Creates a new instance of the result object with the specified code, message
	 * and type
	 * 
	 * @param code
	 * @param message
	 * @param type
	 */
	public Result(String code, String message, ErrorType type) {
		super();
		this.code = code;
		this.type = type;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ErrorType getType() {
		return type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data, Class<?> clazz) {
		this.data = data;
		this.clazz = clazz;
		if (clazz != null)
			this.clazzName = clazz.getName();
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getClazzName() {
		return clazzName;
	}

	/**
	 * Adds a warning to the list of warning in the result object. Sets the error
	 * code and message to those of {@link SystemError#WARNING}
	 * 
	 * @param warning warning to add
	 */
	public void addWarning(String warning) {
		if (getWarnings() == null)
			this.warnings = new ArrayList<String>();
		warnings.add(warning);
		setCode(SystemError.WARNING.getErrorCode());
		setMessage(SystemError.WARNING.getMessage());
		setType(ErrorType.WARNING);

	}

	/**
	 * Adds the list of warnings specified to the result object. Sets the error code
	 * and message to those of {@link SystemError#WARNING}
	 * 
	 * @param warnings warnings to add
	 */
	public void addWarnings(List<String> warnings) {
		if (getWarnings() == null)
			this.warnings = new ArrayList<String>();
		this.warnings.addAll(warnings);
		setCode(SystemError.WARNING.getErrorCode());
		setMessage(SystemError.WARNING.getMessage());
		setType(ErrorType.WARNING);
	}

	/**
	 * Checks if the result object has warnings
	 * 
	 * @return true if the error type is {@link ErrorType#WARNING}
	 */
	public boolean hasWarnings() {
		if (getType().getType().equals(ErrorType.WARNING.getType()))
			return true;
		else
			return false;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * Checks if the result object is success
	 * 
	 * @return true if the error type is {@link ErrorType#SUCCESS} or
	 *         {@link ErrorType#WARNING}
	 */
	public boolean isSuccess() {
		return getType().getType().equals(ErrorType.SUCCESS.getType())
				|| getType().getType().equals(ErrorType.WARNING.getType());
	}

	/**
	 * Checks if the result object has a system error
	 * 
	 * @return true if the error type is {@link ErrorType#SYSTEM_ERROR}
	 */
	public boolean isSystemError() {
		return getType().getType().equals(ErrorType.SYSTEM_ERROR.getType());
	}

	/**
	 * Checks if the result object has a business error
	 * 
	 * @return true if the error type is {@link ErrorType#BUSINESS_ERROR}
	 */
	public boolean isBusinessError() {
		return getType().getType().equals(ErrorType.BUSINESS_ERROR.getType());
	}

	/**
	 * Checks if the result object has an external system error
	 * 
	 * @return true if the error type is {@link ErrorType#SYSTEM_ERROR}
	 */
	public boolean isExternalSystemError() {
		return getType().getType().equals(ErrorType.EXTERNAL_SYSTEM_ERROR.getType());
	}

	/**
	 * Checks if the result object has a validation error
	 * 
	 * @return true if the error type is {@link ErrorType#VALIDATION_ERROR}
	 */
	public boolean isValidationError() {
		return getType().getType().equals(ErrorType.VALIDATION_ERROR.getType());
	}

	public Object getSavepoint() {
		return savepoint == null ? null : savepoint.getSavepoint();
	}

	public void setSavepoint(Object savepoint) {
		this.savepoint = new Savepoint(savepoint);
	}

	/**
	 * Checks if the data type of the data is of the specified class type
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean hasDataType(Class<?> clazz) {

		if (null == clazz)
			return false;

		if (StringUtils.isBlank(clazzName))
			return false;

		return clazzName.equals(clazz.getName());
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", type=" + type + ", message=" + message + "]";
	}

	/**
	 * Generates a result by substituting the variables [var] in the error message
	 * with the values passed to it
	 * 
	 * @param error         error enumeration to generate the message from
	 * @param variableInput variable number of values to be used in the substitution
	 *                      of the [var] entries in the message
	 * @return
	 */
	public static Result generate(ErrorEnum error, Object... variableInput) {
		return generate(error, null, variableInput);
	}

	public Long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(Long processingTime) {
		this.processingTime = processingTime;
	}

	/**
	 * Generates a result by substituting the variables [var] in the error message
	 * with the values passed to it
	 * 
	 * @param error         error enumeration to generate the message from
	 * @param savepoint     savepoint to add to the result object
	 * @param variableInput variable number of values to be used in the substitution
	 *                      of the [var] entries in the message
	 * @return
	 */
	public static Result generate(ErrorEnum error, Savepoint savepoint, Object... variableInput) {

		String code = error.getErrorCode();
		String message = error.getMessage();
		ErrorType type = error.getErrorType();

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

		message = substituteMessage(message, messageArgs);

		Result r = new Result(code, message, type, savepoint, messageArgs);

		return r;
	}

	/**
	 * Generates a localized message for the result based on the language passed to
	 * it
	 * 
	 * @param localeLanguage locale language to get the message for
	 * @return localized message based on language specified. If the locale language
	 *         code specified is not valid or no localized message is found for it,
	 *         the original default English message will be returned
	 */
	public String generateLocalizedMessage(String localeLanguage) {

		if (StringUtils.isBlank(localeLanguage))
			return this.message;

		boolean arabic = false;
		ResourceBundle bundle = translations.get(localeLanguage);
		if (bundle == null) {
			try {
				Locale locale = LocaleUtils.toLocale(localeLanguage);
				if (locale == null) {
					log.warn("Invalid locale [" + localeLanguage + "] specified for localized message. "
							+ "Returning default message");
				} else {
					if (locale.getLanguage().equals("ar"))
						arabic = true;
					try {
						bundle = ResourceBundle.getBundle("errormsg", locale);
					} catch (MissingResourceException e) {
						log.warn(
								"Requested locale [" + localeLanguage + "] does not have any resources using defaults");
					}
				}
			} catch (Exception e) {
				log.warn("Invalid locale [" + localeLanguage
						+ "]specified for localized message. Returning default message");
			}
		}

		if (bundle == null)
			return this.message;

		try {
			List<String> messageArgsToUse = new ArrayList<String>();
			String messageValue = bundle.getString(this.code);
			if (arabic) {
				for (String arg : messageArgs) {
					messageArgsToUse.add(Utils.numericToArabicNumber(arg));
				}
			} else {
				messageArgsToUse = this.messageArgs;
			}
			return substituteMessage(messageValue, messageArgsToUse);

		} catch (Exception e) {
			log.warn("No localized mesage found for error code [" + this.code + "] using default message");
			return this.message;
		}

	}

	/**
	 * Substitutes the variables entries in a message with the variable input
	 * specified if any
	 * 
	 * @param message       message to substitute values in
	 * @param variableInput variable input used for the substitution
	 * @return Substituted string
	 */
	private static String substituteMessage(String message, List<String> variableInput) {

		if (!Utils.emptyList(variableInput)) {
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

	public class Savepoint {

		private Object savepoint;

		public Savepoint(Object savepoint) {
			super();
			this.savepoint = savepoint;
		}

		public Object getSavepoint() {
			return savepoint;
		}

		public void setSavepoint(Object savepoint) {
			this.savepoint = savepoint;
		}

	}

	/**
	 * Checks if the result has the error code for the error enum specified
	 * 
	 * @param errorEnum error enum to check
	 * @return true if the error enum is equal to the error code set for the result
	 *         object
	 */
	public boolean hasErrorCode(ErrorEnum errorEnum) {
		return getCode().equals(errorEnum.getErrorCode());
	}

}
