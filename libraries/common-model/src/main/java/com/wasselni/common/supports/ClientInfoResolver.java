
package com.wasselni.common.supports;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wasselni.common.model.audit.ClientInfo;

public class ClientInfoResolver {

	public ClientInfo resolve() {
		return new ClientInfo(getHttpServletRequest());
	}

	private HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
