
package com.wasselni.notification.server.dao.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wasselni.common.model.emailtemplate.NotificationModel;
import com.wasselni.notification.server.dao.notification.rm.NotificationModelRowMapper;
import com.wasselni.notification.server.dao.notification.sql.NotificationSql;

@Repository
public class NotificationDaoImpl implements INotificationDao {

	private @Autowired JdbcTemplate jdbcTemplate;

	@Override
	public void updateNotificationMail(Long id, boolean success, String errorMessage) {
		jdbcTemplate.update(NotificationSql.UPDATE_EMAIL_NOTIFICATION, success, errorMessage, id);
	}

	@Override
	public void updateNotificationSms(Long id, boolean success, String errorMessage) {
		jdbcTemplate.update(NotificationSql.UPDATE_SMS_NOTIFICATION, success, errorMessage, id);
	}

	@Override
	public void updateNotificationWhatsapp(Long id, boolean success, String errorMessage) {
		jdbcTemplate.update(NotificationSql.UPDATE_WHATSAPP_NOTIFICATION, success, errorMessage, id);
	}

	@Override
	public List<NotificationModel> pendingNotification() {
		return jdbcTemplate.query(NotificationSql.GET_NOTIFICATION_LIST, new NotificationModelRowMapper());
	}

	@Override
	public void notification(NotificationModel notificationModel) {
		jdbcTemplate.update(NotificationSql.ADD_LOG_EMAIL, notificationModel.getRecipient(),
				notificationModel.getSubject(), notificationModel.getText(), notificationModel.isNotifyEmail(),
				notificationModel.isNotifySms(), notificationModel.getCallingCode(),
				notificationModel.getMobileNumber());
	}

}
