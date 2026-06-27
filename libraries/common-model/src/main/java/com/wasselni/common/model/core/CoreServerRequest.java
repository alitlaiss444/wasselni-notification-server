package com.wasselni.common.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreServerRequest {

	private String operation;
	private JsonNode payload;

	public CoreServerRequest() {
		super();
	}

	public <T> CoreServerRequest(T payload, ObjectMapper objectMapper) {
		this.payload = objectMapper.valueToTree(payload);
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public JsonNode getPayload() {
		return payload;
	}

	public void setPayload(JsonNode payload) {
		this.payload = payload;
	}

}
