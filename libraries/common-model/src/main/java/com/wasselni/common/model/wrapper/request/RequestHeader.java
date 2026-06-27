
package com.wasselni.common.model.wrapper.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wasselni.common.model.otp.OtpRequest;

import jakarta.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestHeader {

	private String requestId;
	private String timeStamp;
	private Integer applicationId;
	private String ipAddress;
	private String deviceModel;
	private String operatingSystem;
	private @Valid OtpRequest otpRequest;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public OtpRequest getOtpRequest() {
		return otpRequest;
	}

	public void setOtpRequest(OtpRequest otpRequest) {
		this.otpRequest = otpRequest;
	}

}
