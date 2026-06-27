package com.wasselni.common.model.errors;

import com.wasselni.common.model.errors.constants.ErrorType;
import com.wasselni.common.model.errors.exception.SystemException;

/**
 * Interface to be used and extended by modules. The {@link SystemException}
 * expects an error enum in the constructor from which the code, message and
 * type of error is extracted
 *
 */
public interface ErrorEnum {

	public String getErrorCode();

	public String getMessage();

	public ErrorType getErrorType();

}
