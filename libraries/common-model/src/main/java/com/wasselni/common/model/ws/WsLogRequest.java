
package com.wasselni.common.model.ws;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WsLogRequest<A, B> {

	private String requestType;
	private boolean success;
	private A request;
	private B response;

	public WsLogRequest() {
		super();
	}

	public WsLogRequest(String requestType, boolean success, A request, B response) {
		super();
		this.requestType = requestType;
		this.success = success;
		this.request = request;
		this.response = response;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public A getRequest() {
		return request;
	}

	public void setRequest(A request) {
		this.request = request;
	}

	public B getResponse() {
		return response;
	}

	public void setResponse(B response) {
		this.response = response;
	}

	public String getRequestJson() {

		if (request == null) {
			return null;
		}

		try {
			return new ObjectMapper().writer().writeValueAsString(request);
		} catch (Exception e) {
			return null;
		}
	}

	public String getResponseJson() {

		if (response == null) {
			return null;
		}

		try {
			return new ObjectMapper().writer().writeValueAsString(response);
		} catch (Exception e) {
			return null;
		}
	}

}
