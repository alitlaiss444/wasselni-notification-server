package com.wasselni.common.model.users;

import java.util.Date;

import com.wasselni.common.model.audit.IncludeAudit;
import com.wasselni.common.model.common.DefaultValidationMessage;
import com.wasselni.common.model.common.DefaultValidationPattern;
import com.wasselni.common.model.enums.LocalEnums.Language;
import com.wasselni.common.utils.DateUtils;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateProfileDetailsRequest {

	@NotNull(message = DefaultValidationMessage.EMAIL_ADDRESS + DefaultValidationMessage.MANDATORY)
	@Size(min = 5, max = 250, message = DefaultValidationMessage.EMAIL_ADDRESS + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.EMAIL, message = DefaultValidationMessage.EMAIL_ADDRESS
			+ DefaultValidationMessage.REGEX)
	@IncludeAudit(name = "Email address")
	private String emailAddress;

	@NotNull(message = DefaultValidationMessage.LANGUAGE + DefaultValidationMessage.MANDATORY)
	@Size(min = 2, max = 2, message = DefaultValidationMessage.LANGUAGE + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.ALPHA, message = DefaultValidationMessage.LANGUAGE
			+ DefaultValidationMessage.REGEX)
	private String language;

	@NotNull(message = DefaultValidationMessage.DOB + DefaultValidationMessage.MANDATORY)
	private Date dob;

	@NotNull(message = DefaultValidationMessage.MOBILE_NUMBER + DefaultValidationMessage.MANDATORY)
	@Size(min = 5, max = 50, message = DefaultValidationMessage.MOBILE_NUMBER + DefaultValidationMessage.SIZE)
	@Pattern(regexp = DefaultValidationPattern.NUMERIC, message = DefaultValidationMessage.MOBILE_NUMBER
			+ DefaultValidationMessage.REGEX)
	@IncludeAudit(name = "Mobile number")
	private String mobileNumber;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@AssertTrue(message = DefaultValidationMessage.LANGUAGE_NOT_VALID)
	private boolean isValidLanguage() {

		if (Language.getLanguage(language.toUpperCase()) == null) {
			return false;
		}

		this.language = this.language.toUpperCase();

		return true;

	}

	@AssertTrue(message = DefaultValidationMessage.DOB)
	private boolean isValidDob() {

		if (dob == null) {
			return false;
		}

		Date minimumDateAge = DateUtils.addYearsToDate(new Date(), -18);

		if (minimumDateAge.before(dob)) {
			return false;
		}

		return true;

	}

}
