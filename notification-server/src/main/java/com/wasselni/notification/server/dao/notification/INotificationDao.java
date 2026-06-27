package com.wasselni.notification.server.dao.notification;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wasselni.common.model.emailtemplate.NotificationModel;

@Repository
public interface INotificationDao {

	public void updateNotificationMail(Long id, boolean success, String errorMessage);

	public void updateNotificationSms(Long id, boolean success, String errorMessage);

	public void updateNotificationWhatsapp(Long id, boolean success, String errorMessage);

	public List<NotificationModel> pendingNotification();

	public void notification(NotificationModel notificationModel);

}
