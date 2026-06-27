package com.wasselni.common.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wasselni.common.model.errors.constants.SystemError;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreServerResponse {

	private String responseId;
	private boolean success;
	private String code;
	private String message;
	private Object payload;

	public static CoreServerResponse error(String message) {

		CoreServerResponse response = new CoreServerResponse();

		response.setSuccess(false);
		response.setCode(SystemError.UNKNOWN_ERROR.getErrorCode());
		response.setMessage(message);

		return response;
	}

	public static CoreServerResponse success(Object payload) {

		CoreServerResponse response = new CoreServerResponse();

		response.setSuccess(true);
		response.setCode(SystemError.SUCCESS.getErrorCode());
		response.setPayload(payload);

		return response;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
