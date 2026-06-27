package com.wasselni.common.model.login;

import com.wasselni.common.model.audit.IncludeAudit;
import com.wasselni.common.model.common.DefaultValidationMessage;
import com.wasselni.common.model.common.DefaultValidationPattern;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {

	@NotBlank(message = DefaultValidationMessage.MOBILE_NUMBER + DefaultValidationMessage.MANDATORY)
	@Size(min = 6, max = 15, message = DefaultValidationMessage.MOBILE_NUMBER + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.NUMERIC, message = DefaultValidationMessage.MOBILE_NUMBER
			+ DefaultValidationMessage.REGEX)
	@IncludeAudit(name = "Mobile Number")
	private String mobileNumber;

	@NotBlank(message = DefaultValidationMessage.CALLING_CODE + DefaultValidationMessage.MANDATORY)
	@Size(min = 1, max = 4, message = DefaultValidationMessage.CALLING_CODE + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.NUMERIC, message = DefaultValidationMessage.CALLING_CODE
			+ DefaultValidationMessage.REGEX)
	@IncludeAudit(name = "Calling Code")
	private String callingCode;

	private String password;

	public LoginRequest() {
		super();
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
