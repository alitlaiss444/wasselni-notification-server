
package com.wasselni.common.model.wrapper.response;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.localization.LocalizeSerializer;
import com.wasselni.common.model.otp.OtpResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeader {

	private String responseCode = SystemError.SUCCESS.getErrorCode();
	private @JsonSerialize(using = LocalizeSerializer.class) String message = SystemError.SUCCESS.getMessage();
	private String responseId;
	private String timeStamp;
	private OtpResponse otpResponse;

	public ResponseHeader() {
		super();
		this.timeStamp = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss").format(new Date());
	}

	public ResponseHeader(String message) {
		super();
		this.timeStamp = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss").format(new Date());
		this.message = message;
	}

	public ResponseHeader(String responseId, String message) {
		super();
		this.responseId = responseId;
		this.message = message;
	}

	public ResponseHeader(String responseCode, String responseId, String message) {
		super();
		this.responseCode = responseCode;
		this.responseId = responseId;
		this.timeStamp = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss").format(new Date());
		this.message = message;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public OtpResponse getOtpResponse() {
		return otpResponse;
	}

	public void setOtpResponse(OtpResponse otpResponse) {
		this.otpResponse = otpResponse;
	}

}
