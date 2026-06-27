package com.wasselni.notification.server.dao.notification;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.wasselni.notification.server.dao.notification.rm.SmtpSettingsRowMapper;
import com.wasselni.notification.server.model.ApiException;
import com.wasselni.notification.server.model.SmtpConfig;
import com.wasselni.notification.server.model.SmtpSettings;
import com.wasselni.notification.server.services.JasyptUtils;

@Component("smtpSettingsHelper")
public class SmtpSettingsHelper {

	private static final Logger log = LogManager.getLogger(SmtpSettingsHelper.class);

	private @Autowired SmtpConfig smtpConfig;
	private @Autowired JdbcTemplate jdbcTemplate;

	public SmtpConfig getSmtpConfig() {
		return smtpConfig;
	}

	public void init() throws ApiException {

		// Load SMTP Setting

		List<SmtpSettings> smtpList = jdbcTemplate.query(
				"select host,port,binding_address,binding_password,outgoing_from,authentication_required from smtp_configuration",
				new SmtpSettingsRowMapper());

		if (smtpList == null || smtpList.isEmpty() || smtpConfig == null) {
			throw new ApiException("001", "SMTP Settings not defined properly");
		}

		for (SmtpSettings item : smtpList) {

			if (StringUtils.isBlank(item.getBindingPassword())) {
				continue;
			}

			item.setBindingPassword(JasyptUtils.defaultDecrypt(item.getBindingPassword()));
		}

		smtpConfig.setSmtpSettings(smtpList.get(0));

		log.debug(this.smtpConfig.toString());
	}

}
