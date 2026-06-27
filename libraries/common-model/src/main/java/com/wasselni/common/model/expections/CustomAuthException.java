
package com.wasselni.common.model.expections;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	private String username;

	public CustomAuthException(String message, String username) {
		super(message);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
