
package com.wasselni.notification.server.model;

public class SmtpSettings {

	private String host;
	private int port;
	private String bindingAddress;
	private String bindingPassword;
	private String outgoingFrom;
	private boolean authenticationRequired;

	public SmtpSettings() {
		super();
	}

	public SmtpSettings(String host, int port, String bindingAddress, String bindingPassword, String outgoingFrom,
			boolean authenticationRequired) {
		super();
		this.host = host;
		this.port = port;
		this.bindingAddress = bindingAddress;
		this.bindingPassword = bindingPassword;
		this.outgoingFrom = outgoingFrom;
		this.authenticationRequired = authenticationRequired;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBindingAddress() {
		return bindingAddress;
	}

	public void setBindingAddress(String bindingAddress) {
		this.bindingAddress = bindingAddress;
	}

	public String getBindingPassword() {
		return bindingPassword;
	}

	public void setBindingPassword(String bindingPassword) {
		this.bindingPassword = bindingPassword;
	}

	public String getOutgoingFrom() {
		return outgoingFrom;
	}

	public void setOutgoingFrom(String outgoingFrom) {
		this.outgoingFrom = outgoingFrom;
	}

	public boolean isAuthenticationRequired() {
		return authenticationRequired;
	}

	public void setAuthenticationRequired(boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}

	@Override
	public String toString() {
		return "SmtpSettings [host=" + host + ", port=" + port + ", bindingAddress=" + bindingAddress
				+ ", bindingPassword=" + bindingPassword + ", outgoingFrom=" + outgoingFrom
				+ ", authenticationRequired=" + authenticationRequired + "]";
	}

}
