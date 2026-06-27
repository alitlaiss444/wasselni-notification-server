
package com.wasselni.common.model.wrapper.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

	private boolean success;
	private ResponseHeader ResponseHeader;
	private T body;

	public Response() {
		super();
	}

	public Response(boolean success, ResponseHeader responseHeader) {
		this.success = success;
		this.ResponseHeader = responseHeader;
	}

	public Response(boolean success, ResponseHeader responseHeader, T body) {
		this.success = success;
		this.ResponseHeader = responseHeader;
		this.body = body;
	}

	public Response(T body) {
		super();
		this.success = true;
		this.ResponseHeader = new ResponseHeader();
		this.body = body;
	}

	public Response(ResponseHeader responseHeader, T body) {
		this.success = true;
		this.ResponseHeader = responseHeader;
		this.body = body;
	}

	public Response(ResponseHeader responseHeader) {
		this.success = true;
		this.ResponseHeader = responseHeader;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ResponseHeader getResponseHeader() {
		return ResponseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		ResponseHeader = responseHeader;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
