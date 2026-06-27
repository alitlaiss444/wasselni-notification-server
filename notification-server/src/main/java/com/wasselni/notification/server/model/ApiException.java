
package com.wasselni.notification.server.model;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;
	private String description;

	public ApiException(String description) {
		super(description);
		this.description = description;
	}

	public ApiException(String code, String message) {
		super(message);
		this.code = code;
		this.description = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
