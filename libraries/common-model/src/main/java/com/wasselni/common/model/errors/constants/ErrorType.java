package com.wasselni.common.model.errors.constants;

/**
 * Holds all possible result types that might be returned by the rule engine
 * 
 * @author Wassim
 * 
 */

public enum ErrorType {

	VALIDATION_ERROR("Validation Error"),

	BUSINESS_ERROR("Business Error"),

	SYSTEM_ERROR("System Error"),

	EXTERNAL_SYSTEM_ERROR("External System Error"),

	WARNING("Warning"),

	SUCCESS("Success");

	private String type;

	private ErrorType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static ErrorType fromValue(String value) {

		for (ErrorType v : values()) {
			if (v.type.equals(value)) {
				return v;
			}
		}
		return null;
	}

}
