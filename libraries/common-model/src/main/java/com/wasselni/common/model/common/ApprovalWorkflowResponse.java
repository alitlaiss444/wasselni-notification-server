
package com.wasselni.common.model.common;

public class ApprovalWorkflowResponse {

	private String otpUuid;
	private Integer flowStatus;
	private String flowMessage;

	public String getOtpUuid() {
		return otpUuid;
	}

	public void setOtpUuid(String otpUuid) {
		this.otpUuid = otpUuid;
	}

	public Integer getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(Integer flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getFlowMessage() {
		return flowMessage;
	}

	public void setFlowMessage(String flowMessage) {
		this.flowMessage = flowMessage;
	}

}
