package com.wasselni.common.model.users;

import com.wasselni.common.model.common.DefaultValidationMessage;
import com.wasselni.common.model.common.DefaultValidationPattern;
import com.wasselni.common.model.pagging.PageSetup;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GetUsersRequest extends PageSetup {

	@Size(min = 3, max = 100, message = DefaultValidationMessage.USERNAME + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.ALPHA_NUMERIC_WITH_SYMBOLS, message = DefaultValidationMessage.USERNAME
			+ DefaultValidationMessage.REGEX)
	private String username;

	private Boolean userLocked;

	private Integer userStatus;

	@Size(min = 3, max = 15, message = DefaultValidationMessage.MOBILE_NUMBER + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.NUMERIC, message = DefaultValidationMessage.MOBILE_NUMBER
			+ DefaultValidationMessage.REGEX)
	private String mobileNumber;

	private String entityCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getUserLocked() {
		return userLocked;
	}

	public void setUserLocked(Boolean userLocked) {
		this.userLocked = userLocked;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

}
