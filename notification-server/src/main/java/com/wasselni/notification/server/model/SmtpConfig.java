
package com.wasselni.notification.server.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmtpConfig {

	private SmtpSettings smtpSettings;
	private @Value("${smtp.isDev:false}") boolean dev;
	private @Value("${smtp.enabled:true}") Boolean smtpEnabled;

	public SmtpConfig() {
		super();
	}

	public SmtpConfig(SmtpSettings smtpSettings, boolean dev, boolean smtpEnabled) {
		super();
		this.smtpSettings = smtpSettings;
		this.dev = dev;
		this.smtpEnabled = smtpEnabled;
	}

	public SmtpSettings getSmtpSettings() {
		return smtpSettings;
	}

	public void setSmtpSettings(SmtpSettings smtpSettings) {
		this.smtpSettings = smtpSettings;
	}

	public boolean isDev() {
		return dev;
	}

	public void setDev(boolean dev) {
		this.dev = dev;
	}

	public Boolean getSmtpEnabled() {
		return smtpEnabled;
	}

	public void setSmtpEnabled(Boolean smtpEnabled) {
		this.smtpEnabled = smtpEnabled;
	}

	@Override
	public String toString() {
		return "SmtpConfig [smtpSettings=" + smtpSettings + ", dev=" + dev + ", smtpEnabled=" + smtpEnabled + "]";
	}

}
