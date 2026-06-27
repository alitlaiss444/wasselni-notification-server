package com.wasselni.common.model.errors.exception;

import com.wasselni.common.model.errors.ErrorEnum;
import com.wasselni.common.model.errors.domain.Result;

/**
 * 
 * Common exception to be used across the libraries and modules. It holds a
 * result object with the details of the exception along with a message
 *
 */
public class SystemException extends Exception {

	private static final long serialVersionUID = 1L;

	private Result result;
	private String message;

	/**
	 * Generates an exception with the result generated from the {@link ErrorEnum}
	 * specified. The values of [var] in the error message of the result object are
	 * substituted by the variableInput values specified
	 * 
	 * @param result        {@link ErrorEnum} to use to get the code, message and
	 *                      type from
	 * @param variableInput values to use in the substitutions of [var] in the error
	 *                      message
	 */
	public SystemException(ErrorEnum result, Object... variableInput) {
		super();
		this.result = Result.generate(result, variableInput);
		this.message = this.result.getMessage();
	}

	/**
	 * Generates an exception with the result specified
	 * 
	 * @param result {@link Result} object holding the details of the exception
	 */
	public SystemException(Result result) {
		this.result = result;
		this.message = this.result.getMessage();

	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
