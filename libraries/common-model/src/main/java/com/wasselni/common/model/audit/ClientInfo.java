package com.wasselni.common.model.audit;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public final class ClientInfo {

	public static ClientInfo EMPTY_CLIENT_INFO = new ClientInfo();

	private final String serverIpAddress;
	private final String uri;
	private final String contextRoot;

	private ClientInfo() {
		this((String) null, (String) null, (String) null);
	}

	public ClientInfo(final HttpServletRequest request) {
		this(request.getLocalAddr(), request.getServletPath(), request.getContextPath());
	}

	public ClientInfo(final HttpServletRequest request, final String alternateLocation) {
		this(request.getLocalAddr(), request.getServletPath(), request.getContextPath());
	}

	public ClientInfo(final String serverIpAddress, String uri, String contextRoot) {
		this.serverIpAddress = serverIpAddress == null ? "unknown" : serverIpAddress;
		this.uri = uri;
		this.contextRoot = contextRoot;
	}

	public String getServerIpAddress() {
		return this.serverIpAddress;
	}

	public String getUri() {
		return uri;
	}

	public String getContextRoot() {
		if (!StringUtils.isBlank(contextRoot)) {
			return contextRoot.replace("/", "").toUpperCase();
		} else {
			return "unknown";
		}
	}

}
