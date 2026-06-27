
package com.wasselni.common.model.common;

public class HealthResponse {

	private String status;

	public HealthResponse() {
		super();
		this.status = "UP";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
