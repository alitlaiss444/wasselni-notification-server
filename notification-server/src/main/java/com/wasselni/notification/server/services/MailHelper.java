package com.wasselni.notification.server.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.notification.server.dao.notification.INotificationDao;
import com.wasselni.notification.server.dao.notification.SmtpSettingsHelper;
import com.wasselni.notification.server.model.ApiException;
import com.wasselni.notification.server.model.SmtpSettings;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;

@Service("mailHelper")
public class MailHelper {

	private static final Logger log = LogManager.getLogger(MailHelper.class);

	private @Autowired SmtpSettingsHelper smtpSettingsHelper;
	private @Autowired INotificationDao notificationDao;

	@PostConstruct
	public void init() throws ApiException {
		smtpSettingsHelper.init();
	}

	/**
	 * Sends an email message
	 *
	 * @param recipient    recipient to send the email to
	 * @param subject      subject of the email
	 * @param text         text of the email
	 * @param html         HTML text of the email
	 * @param attachements list of attacements to send
	 * @throws SystemException
	 */
	public void sendMessage(String recipient, String subject, String text, String html, List<File> attachements,
			Long id) throws ApiException {

		boolean success = true;
		String error = null;

		try {

			if (!smtpSettingsHelper.getSmtpConfig().getSmtpEnabled()) {
				log.debug("SMTP disabled");
				return;
			}

			JavaMailSenderImpl sender = new JavaMailSenderImpl();

			SmtpSettings smtpSetting = smtpSettingsHelper.getSmtpConfig().getSmtpSettings();

			if (smtpSetting == null) {
				log.error("Failed sending email, no smtp settings found");
				throw new ApiException("0001", "Failed sending email");
			}

			sender.setHost(smtpSetting.getHost());
			sender.setPort(smtpSetting.getPort());
			sender.setDefaultEncoding("Windows-1256");

			if (smtpSetting.isAuthenticationRequired()) {
				sender.setUsername(smtpSetting.getBindingAddress());
				sender.setPassword(smtpSetting.getBindingPassword());
			}

			log.debug("Sending mail "
					+ String.format("Recipients[%s], Subject[%s] Attachments[%s]", recipient, subject, attachements));

			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(smtpSetting.getOutgoingFrom());
			List<String> sendTo = new ArrayList<>();
			sendTo.add(recipient);
			helper.setTo(sendTo.toArray(new String[] {}));
			helper.setSubject(subject);

			helper.setText(text, html);

			ClassPathResource logo = new ClassPathResource("logo.jpg");
			helper.addInline("bank-logo", logo);

			if (attachements != null && !attachements.isEmpty()) {
				for (File attachement : attachements) {
					helper.addAttachment(attachement.getName(), attachement);
				}
			}

			if (smtpSettingsHelper.getSmtpConfig().isDev()) {
				Properties props = sender.getJavaMailProperties();
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.debug", "true");
				if (smtpSetting.isAuthenticationRequired()) {
					props.put("mail.smtp.ssl.trust", smtpSetting.getHost());
				}
			}

			sender.send(message);
			log.debug("Email sent successfully");

		} catch (Exception e) {
			success = false;
			log.error("Failed sending email", e);
			error = "Failed sending email [" + e.getMessage() + "]";
		} finally {
			notificationDao.updateNotificationMail(id, success, error);
		}
	}

}
