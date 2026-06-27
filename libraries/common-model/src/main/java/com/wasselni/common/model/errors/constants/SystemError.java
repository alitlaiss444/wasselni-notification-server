package com.wasselni.common.model.errors.constants;

import com.wasselni.common.model.errors.ErrorEnum;

public enum SystemError implements ErrorEnum {

	/*
	 * ---------------------------- Core Error ----------------------------
	 */

	CORE_SERVER_CONNECTION_FAILED("C001", "{[var]}", ErrorType.SYSTEM_ERROR),

	CORE_SERVER_UNSUCCESS_RESPONSE("C002", "{[var]}", ErrorType.VALIDATION_ERROR),

	/*
	 * ---------------------------- Success ----------------------------
	 */

	SUCCESS("000", "Operation Successful", ErrorType.SUCCESS),

	WARNING("001", "Operation Successful with Warnings", ErrorType.WARNING),

	SUCCESS_APPROVAL_FLOW("002", "{success_approval_flow}", ErrorType.SUCCESS),

	OTP_FAILURE("003", "{otp_failure}", ErrorType.SYSTEM_ERROR),

	OTP_FAILURE_VERIFICATION("004", "{otp_failure_verification}", ErrorType.SYSTEM_ERROR),

	OTP_FAILURE_MAX_TRIES("005", "{otp_failure_max_tries}", ErrorType.SYSTEM_ERROR),

	OTP_FAILURE_EXISTS("006", "{otp_verified}", ErrorType.SYSTEM_ERROR),

	OTP_FAILURE_EXPIRED("006", "{otp_failure_expired}", ErrorType.SYSTEM_ERROR),

	PASSWORD_NO_MATCH("007", "{password_not_match}", ErrorType.SYSTEM_ERROR),

	PASSWORD_STRENGTH("008", "{password_stregnth}", ErrorType.SYSTEM_ERROR),

	PASSWORD_WRONG("009", "{wrong_password}", ErrorType.SYSTEM_ERROR),

	LDAP_USER_NOT_FOUND("010", "{ldap_user_not_found}", ErrorType.SYSTEM_ERROR),

	USER_EXISTS_USE_UPDATE("011", "{user_exist_use_update}", ErrorType.SYSTEM_ERROR),

	USER_EXISTS("012", "{user_exists}", ErrorType.SYSTEM_ERROR),

	PASSWORD_EXISTS("013", "{password_exists}", ErrorType.SYSTEM_ERROR),

	NOT_ALLOWED_PATERN("014", "{user_not_allowed_pattern}", ErrorType.SYSTEM_ERROR),

	USER_LOCKED("015", "{user_locked}", ErrorType.SYSTEM_ERROR),

	USER_NOT_ACTIVE("016", "{user_not_active}", ErrorType.SYSTEM_ERROR),

	USER_REACH_LIMIT("017", "{user_reach_limit}", ErrorType.SYSTEM_ERROR),

	USER_NOT_FOUND("018", "{user_not_found}", ErrorType.SYSTEM_ERROR),

	USER_NOT_VERIFIED("019", "{user_not_verified}", ErrorType.SYSTEM_ERROR),

	/*
	 * --------------------- Validation Errors ------------------------
	 */
	VE_MISSING_MANDATORY("VE001", "Missing mandatory parameter [var]", ErrorType.VALIDATION_ERROR),

	VE_LENGTH_LESS_THAN_MINIMUM("VE002",
			"Field [var] has value [var] with length [var] less than minimum allowed [var]",
			ErrorType.VALIDATION_ERROR),

	VE_LENGTH_GREATER_THAN_MAXIMUM("VE003",
			"Field [var] has value [var] with length [var] greater than the maximum allowed [var]",
			ErrorType.VALIDATION_ERROR),

	VE_VALUE_GREATER_THAN_MAXIMUM("VE004", "Field [var] has value [var] greater than the maximum allowed [var]",
			ErrorType.VALIDATION_ERROR),

	VE_VALUE_LESS_THAN_MINIMUM("VE005", "Field [var] has value [var] less than the minimum allowed [var]",
			ErrorType.VALIDATION_ERROR),

	VE_INVALID_FIELD_VALUE("VE006", "Invalid value [var] for [var]", ErrorType.VALIDATION_ERROR),

	VE_INVALID_FIELD_VALUE_SUPPORTED("VE007", "Invalid value [var] for [var]. Supported values [var]",
			ErrorType.VALIDATION_ERROR),

	VE_FIELD_IS_NOT_ALLOWED_FOR_UPDATE("VE008", "Field [var] is not allowed for update", ErrorType.VALIDATION_ERROR),

	VE_GENERIC("VE009", "Validation Error [var]", ErrorType.VALIDATION_ERROR),

	VE_API_VALIDATION("VE010", " [var]", ErrorType.VALIDATION_ERROR),

	VE_ACTION_NOT_ALLOWED("VE011", "{ve_action_not_allowed}", ErrorType.VALIDATION_ERROR),

	VE_VALUE_MAIL_NOT_IN_FORMAT("VE011", "Invalid mail format [var]", ErrorType.VALIDATION_ERROR),

	VE_VALUE_MOBILE_NOT_IN_FORMAT("VE012", "Invalid [var] format with value [var]", ErrorType.VALIDATION_ERROR),

	VE_DETAILS_NOT_FOUND("VE013", "{details_not_found}", ErrorType.VALIDATION_ERROR),

	VE_API_INTERNAL_VALIDATION("VE014", "[var]", ErrorType.VALIDATION_ERROR),

	// Data format related
	DF_PATTERN_SPECIFIES_FIELD_NOT_FOUND("DF001",
			"Error generating [var]. Pattern specifies [var] but it's not provided", ErrorType.BUSINESS_ERROR),

	DF_PATTERN_INVALID_FIELD_LENGTH("DF002",
			"Error generating [var]. Field [var] has length [var] [var] then expected [var]", ErrorType.BUSINESS_ERROR),

	DF_INVALID_CONFIG_FILE("DF003", "Invalid configurtion file [var] for [var]. Reason [var]",
			ErrorType.VALIDATION_ERROR),

	DF_PARSER_MISSING_MANDATORY_TAG("DF004", "File [var] for [var]: Missing mandatory tag [var]",
			ErrorType.VALIDATION_ERROR),

	DF_PARSER_INVALID_TAG_VALUE("DF005",
			"File [var] for [var]: Invalid value [var] " + "for tag [var]. Supported Values [var]",
			ErrorType.VALIDATION_ERROR),

	DF_PARSER_INVALID_VAR_TAG("DF006",
			"File [var] for [var]: Invalid value [var] " + "for tag [var] under [var]. Supported Values [var]",
			ErrorType.VALIDATION_ERROR),

	DF_PARSER_INVALID_VAR_TAG_VALUE("DF007",
			"File [var] for [var]: Invalid value [var] " + "for tag [var] under [var]. Error [var]",
			ErrorType.VALIDATION_ERROR),

	DF_PARSER_MISSING_MANDATORY_TAG_FOR_VAR("DF008",
			"File [var] for [var]: Missing mandatory tag [var] under " + "variable [var]", ErrorType.VALIDATION_ERROR),

	DF_PARSER_CONFIG_FILE_NOT_FOUND("DF009", "Import configuration file [var] for [var] not found",
			ErrorType.VALIDATION_ERROR),

	DF_INVALID_LINE_PARTS("DF010", "Invalid line number of parts. Expected [var] Actual [var]",
			ErrorType.VALIDATION_ERROR),

	DF_INVALID_LINE_LENGTH("DF011", "Line has invalid length. Expected [var]. Actual [var]",
			ErrorType.VALIDATION_ERROR),

	DF_EMPTY_LINE("DF012", "Line [var] is empty.", ErrorType.VALIDATION_ERROR),

	/*
	 * ----------------- Command Line Parsing Errors---------------
	 */

	CLP_TYPE_NOT_SUPPORTED("CLP001", "Command line paramter type [var] not supported", ErrorType.VALIDATION_ERROR),

	CLP_DUPLICATE_OPTION("CLP002", "Duplicate command line paramter [var]", ErrorType.VALIDATION_ERROR),

	CLP_PARSING_FAILED("CLP003", "Failed parsing command line paramter [var]", ErrorType.VALIDATION_ERROR),

	/*
	 * ----------------- Configuration Parsing Errors---------------
	 */

	CFG_TYPE_NOT_SUPPORTED("CFG001", "Configuration parameter type [var] not supported for entry [var]",
			ErrorType.VALIDATION_ERROR),

	CFG_DUPLICATE_OPTION("CFG002", "Duplicate configuration tag [var]", ErrorType.VALIDATION_ERROR),

	/*
	 * ----------------- Files and Directories related Errors---------------
	 */

	DIR_DIRECTORY_DOES_NOT_EXIST("DIR001", "Directory [var] does not exist", ErrorType.VALIDATION_ERROR),

	DIR_DIRECTORY_DOES_NOT_EXIST_FAILED_CREATING("DIR002", "Directory [var] does not exist and failed creating it",
			ErrorType.VALIDATION_ERROR),

	DIR_NOT_A_DIRECTORY("DIR003", "[var] is not a directory", ErrorType.VALIDATION_ERROR),

	DIR_NO_WRITE_PERMISSION("DIR004", "No permission to write in directory [var]", ErrorType.VALIDATION_ERROR),

	DIR_NO_READ_PERMISSION("DIR005", "No read permission on directory [var]", ErrorType.VALIDATION_ERROR),

	DIR_FILE_DOES_NOT_EXIST("DIR006", "File [var] does not exist", ErrorType.VALIDATION_ERROR),

	DIR_FILE_DOES_NOT_EXIST_FAILED_CREATING("DIR007", "File [var] does not exist and failed creating it",
			ErrorType.VALIDATION_ERROR),

	DIR_NOT_A_FILE("DIR008", "[var] is not a file", ErrorType.VALIDATION_ERROR),

	DIR_NO_WRITE_PERMISSION_FILE("DIR009", "No write permission on file [var]", ErrorType.VALIDATION_ERROR),

	DIR_NO_READ_PERMISSION_FILE("DIR010", "No read permission on file [var]", ErrorType.VALIDATION_ERROR),

	DIR_EMPTY_FILE("DIR011", "File [var] is empty", ErrorType.VALIDATION_ERROR),

	DIR_FAILED_CREATING_DIR("DIR012", "Failed creating directory [var]", ErrorType.VALIDATION_ERROR),

	/*
	 * ----------------------- System Errors -----------------------
	 */

	SE_DATABASE("SE001", "Database Error on [var]", ErrorType.SYSTEM_ERROR),

	SE_DATABASE_LOCK("SE002", "Database Error. [var] record with [var] locked", ErrorType.SYSTEM_ERROR),

	SE_ENCRYPTION_ERROR("SE003", "Encryption Error [var]", ErrorType.SYSTEM_ERROR),

	SE_INTERNAL_ERROR("SE995", "{se_internal_error}", ErrorType.SYSTEM_ERROR),

	SE_COMMUNICATION_ERROR("SE996", "Communication error [var]", ErrorType.EXTERNAL_SYSTEM_ERROR),

	SE_EXTERNAL_SYSTEM_ERROR("SE997", "External System Error [var]", ErrorType.EXTERNAL_SYSTEM_ERROR),

	SE_TIME_OUT("SE998", "Operation Timed-out [var]", ErrorType.SYSTEM_ERROR),

	SE_SYSTEM_ERROR("SE999", "System Error [var]", ErrorType.SYSTEM_ERROR),

	/*
	 * ----------------------- End OF Errors ------------------------------- This
	 * This should be the last enumeration in the list
	 */
	UNKNOWN_ERROR("999", "Unknown System Error", ErrorType.SYSTEM_ERROR);

	// message code default to 999 unknown system error
	private String errorCode = "999";
	// error message defaulted to unknown system error
	private String message = "Unknown System Error";
	// error type defaulted to system error
	private ErrorType errorType = ErrorType.SYSTEM_ERROR;

	SystemError(String code, String message, ErrorType type) {
		this.errorCode = code;
		this.message = message;
		this.errorType = type;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

}
