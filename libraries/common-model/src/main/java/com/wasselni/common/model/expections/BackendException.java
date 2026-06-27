
package com.wasselni.common.model.expections;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.localization.LocalizeSerializer;
import com.wasselni.common.utils.DateUtils;

public class BackendException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private SystemError systemError;
	private String functionName;
	private String internalMessage;
	private @JsonSerialize(using = LocalizeSerializer.class) String systemErrorMessage;

	public BackendException(SystemError systemError) {
		super(systemError.getMessage());
		this.systemError = systemError;
		this.functionName = " ";
		this.systemErrorMessage = systemError.getMessage();
	}

	public BackendException(SystemError systemError, String functionName) {
		super(systemError.getMessage());
		this.systemError = systemError;
		this.functionName = functionName;
		this.systemErrorMessage = systemError.getMessage();
	}

	public BackendException(SystemError systemError, String functionName, String internalMessage,
			Object... variableInput) {
		super(internalMessage);

		// update message
		this.systemErrorMessage = generate(systemError.getMessage(), variableInput);

		this.systemError = systemError;
		this.functionName = functionName;
		this.internalMessage = internalMessage;
	}

	public SystemError getSystemError() {
		return systemError;
	}

	public void setSystemError(SystemError systemError) {
		this.systemError = systemError;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getInternalMessage() {
		return internalMessage;
	}

	public void setInternalMessage(String internalMessage) {
		this.internalMessage = internalMessage;
	}

	public String getSystemErrorMessage() {
		return systemErrorMessage;
	}

	public void setSystemErrorMessage(String systemErrorMessage) {
		this.systemErrorMessage = systemErrorMessage;
	}

	public String toLogString() {

		if (StringUtils.isBlank(internalMessage)) {
			return String.format("Error during [%s]. Message [%s]", functionName, systemError.getMessage());
		}

		return String.format("Error during [%s]. Message [%s]", functionName, internalMessage);
	}

	private static String generate(String message, Object... variableInput) {

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

		return substituteMessage(message, messageArgs);
	}

	private static String substituteMessage(String message, List<String> variableInput) {

		if (variableInput != null && !variableInput.isEmpty()) {
			for (Object object : variableInput) {

				Object susbstituionValue = object;
				if (object instanceof Date && object != null)
					susbstituionValue = DateUtils.formatDateForDisplay((Date) object);
				else if (object instanceof Timestamp && object != null)
					susbstituionValue = DateUtils.formatDateTimeForDisplay((Timestamp) object);

				message = message.replaceFirst("\\[var\\]",
						Matcher.quoteReplacement(String.valueOf(susbstituionValue)));
			}
		}

		return message;
	}

}