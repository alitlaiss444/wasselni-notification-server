
package com.wasselni.common.model.emailtemplate;

import java.util.List;

public class NotificationModel {

	private Long notificationId;
	private String recipient;
	private String subject;
	private String text;
	private String callingCode;
	private String mobileNumber;
	private boolean notifyEmail;
	private boolean notifySms;
	private boolean notifyWhatsapp;

	private List<String> attachements;

	public NotificationModel() {
		super();
	}

	public NotificationModel(String recipient, String subject, String text, String callingCode, String mobileNumber,
			boolean notifyEmail, boolean notifySms, boolean notifyWhatsapp) {
		super();
		this.recipient = recipient;
		this.subject = subject;
		this.text = text;
		this.callingCode = callingCode;
		this.mobileNumber = mobileNumber;
		this.notifyEmail = notifyEmail;
		this.notifySms = notifySms;
		this.notifyWhatsapp = notifyWhatsapp;
	}

	public NotificationModel(String recipient, String subject, String text, String callingCode, String mobileNumber,
			boolean notifyEmail, boolean notifySms, boolean notifyWhatsapp, List<String> attachements) {
		super();
		this.recipient = recipient;
		this.subject = subject;
		this.text = text;
		this.callingCode = callingCode;
		this.mobileNumber = mobileNumber;
		this.notifyEmail = notifyEmail;
		this.notifySms = notifySms;
		this.notifyWhatsapp = notifyWhatsapp;
		this.attachements = attachements;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean isNotifyEmail() {
		return notifyEmail;
	}

	public void setNotifyEmail(boolean notifyEmail) {
		this.notifyEmail = notifyEmail;
	}

	public boolean isNotifySms() {
		return notifySms;
	}

	public void setNotifySms(boolean notifySms) {
		this.notifySms = notifySms;
	}

	public boolean isNotifyWhatsapp() {
		return notifyWhatsapp;
	}

	public void setNotifyWhatsapp(boolean notifyWhatsapp) {
		this.notifyWhatsapp = notifyWhatsapp;
	}

	public List<String> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<String> attachements) {
		this.attachements = attachements;
	}

}
