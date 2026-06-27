package com.wasselni.notification.server.dao.notification.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wasselni.notification.server.model.SmtpSettings;

public class SmtpSettingsRowMapper implements RowMapper<SmtpSettings> {
	@Override
	public SmtpSettings mapRow(ResultSet rs, int rowNum) throws SQLException {

		SmtpSettings model = new SmtpSettings();

		model.setHost(rs.getString(1));
		model.setPort(rs.getInt(2));
		model.setBindingAddress(rs.getString(3));
		model.setBindingPassword(rs.getString(4));
		model.setOutgoingFrom(rs.getString(5));
		model.setAuthenticationRequired(rs.getBoolean(6));

		return model;
	}
}
