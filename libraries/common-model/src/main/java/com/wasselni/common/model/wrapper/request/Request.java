
package com.wasselni.common.model.wrapper.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request<T> {

	private @Valid RequestHeader requestHeader;
	private T body;

	public Request() {
		super();
	}

	public Request(T body) {
		super();
		this.body = body;
	}

	public Request(RequestHeader requestHeader, T body) {
		super();
		this.requestHeader = requestHeader;
		this.body = body;
	}

	public RequestHeader getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
