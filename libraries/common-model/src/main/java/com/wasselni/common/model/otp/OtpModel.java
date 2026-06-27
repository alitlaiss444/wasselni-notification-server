package com.wasselni.common.model.otp;

import java.sql.Timestamp;

public class OtpModel {

	private Long userId;
	private Long otpId;
	private Long endpointKey;
	private String otp;
	private String channel;
	private Timestamp expiryDate;
	private Integer tries;
	private Integer resetTries;
	private Boolean verified;
	private String callingCode;
	private String mobileNumber;
	private Short otpValidityCounts;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOtpId() {
		return otpId;
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public Long getEndpointKey() {
		return endpointKey;
	}

	public void setEndpointKey(Long endpointKey) {
		this.endpointKey = endpointKey;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getTries() {
		return tries;
	}

	public void setTries(Integer tries) {
		this.tries = tries;
	}

	public Integer getResetTries() {
		return resetTries;
	}

	public void setResetTries(Integer resetTries) {
		this.resetTries = resetTries;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Short getOtpValidityCounts() {
		return otpValidityCounts;
	}

	public void setOtpValidityCounts(Short otpValidityCounts) {
		this.otpValidityCounts = otpValidityCounts;
	}

}
