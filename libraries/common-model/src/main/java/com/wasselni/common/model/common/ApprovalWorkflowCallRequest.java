package com.wasselni.common.model.common;

public class ApprovalWorkflowCallRequest<T> {

	private String service;
	private T requestObject;
	private boolean createRequest;

	public T getRequestObject() {
		return requestObject;
	}

	public void setRequestObject(T requestObject) {
		this.requestObject = requestObject;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean isCreateRequest() {
		return createRequest;
	}

	public void setCreateRequest(boolean createRequest) {
		this.createRequest = createRequest;
	}
}
