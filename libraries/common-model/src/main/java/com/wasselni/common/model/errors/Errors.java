package com.wasselni.common.model.errors;

import com.wasselni.common.model.errors.constants.ErrorType;
import com.wasselni.common.model.errors.constants.SystemError;

/**
 * Result Map holding all possible errors returned by any functionality in the
 * rule engine. The BusinessException used throughout the engine will have as a
 * parameter a Result enumeration followed by a variable number of input
 * depending on the error. This variable number of input will replace the [var]
 * values in the order received
 * 
 * @author Wassim
 * 
 */
public enum Errors implements ErrorEnum {

	/*
	 * --------------------- Success ------------------------
	 */
	SUCCESS(SystemError.SUCCESS.getErrorCode(), SystemError.SUCCESS.getMessage(), SystemError.SUCCESS.getErrorType()),

	/*
	 * ----------------------- System Errors -----------------------
	 */

	SE_DATABASE(SystemError.SE_DATABASE.getErrorCode(), SystemError.SE_DATABASE.getMessage(),
			SystemError.SE_DATABASE.getErrorType()),

	SE_DATABASE_LOCK(SystemError.SE_DATABASE_LOCK.getErrorCode(), SystemError.SE_DATABASE_LOCK.getMessage(),
			SystemError.SE_DATABASE.getErrorType()),

	SE_SYSTEM_ERROR(SystemError.SE_SYSTEM_ERROR.getErrorCode(), SystemError.SE_SYSTEM_ERROR.getMessage(),
			SystemError.SE_SYSTEM_ERROR.getErrorType()),

	SE_EXTERNAL_SYSTEM_ERROR(SystemError.SE_EXTERNAL_SYSTEM_ERROR.getErrorCode(),
			SystemError.SE_EXTERNAL_SYSTEM_ERROR.getMessage(), SystemError.SE_EXTERNAL_SYSTEM_ERROR.getErrorType()),

	/*
	 * ----------------------- End OF Errors ----------------------- This This
	 * should be the last enumeration in the list
	 */

	UNKNOWN_ERROR(SystemError.UNKNOWN_ERROR.getErrorCode(), SystemError.UNKNOWN_ERROR.getMessage(),
			SystemError.UNKNOWN_ERROR.getErrorType());

	// message code default to 999 unknown system error
	private String errorCode = SystemError.UNKNOWN_ERROR.getErrorCode();
	// error message defaulted to unknown system error
	private String message = SystemError.UNKNOWN_ERROR.getMessage();
	// error type defaulted to system error
	private ErrorType errorType = SystemError.UNKNOWN_ERROR.getErrorType();

	Errors(String code, String message, ErrorType type) {
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
