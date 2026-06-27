package com.wasselni.common.model.otp.generate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateOtpRequest {

	private String actionForm;
	private Long otpId;
	private Long userId;

	public String getActionForm() {
		return actionForm;
	}

	public void setActionForm(String actionForm) {
		this.actionForm = actionForm;
	}

	public Long getOtpId() {
		return otpId;
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
