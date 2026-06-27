package com.wasselni.notification.server.dao.notification.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wasselni.common.model.emailtemplate.NotificationModel;

public class NotificationModelRowMapper implements RowMapper<NotificationModel> {
	@Override
	public NotificationModel mapRow(ResultSet rs, int rowNum) throws SQLException {

		int i = 1;

		NotificationModel model = new NotificationModel();

		model.setNotificationId(rs.getLong(i++));
		model.setRecipient(rs.getString(i++));
		model.setSubject(rs.getString(i++));
		model.setText(rs.getString(i++));
		model.setCallingCode(rs.getString(i++));
		model.setMobileNumber(rs.getString(i++));
		model.setNotifyEmail(rs.getBoolean(i++));
		model.setNotifySms(rs.getBoolean(i++));
		model.setNotifyWhatsapp(rs.getBoolean(i++));

		return model;
	}
}
