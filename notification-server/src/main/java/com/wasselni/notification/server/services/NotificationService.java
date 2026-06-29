package com.wasselni.notification.server.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wasselni.common.model.emailtemplate.NotificationModel;
import com.wasselni.notification.server.dao.notification.INotificationDao;

@Service
@EnableScheduling
public class NotificationService {

	private static final Logger log = LogManager.getLogger(NotificationService.class);

	private static final String fnSendNotification = "Sending notification";

	private @Autowired INotificationDao notificationDao;
	private @Autowired MailHelper mailHelper;
	private @Autowired WhatsappSenderService whatsappSenderService;

	@Scheduled(fixedRate = 1000)
	public void runJob() throws InterruptedException {

		try {

			List<NotificationModel> list = notificationDao.pendingNotification();

			if (list.size() > 0) {
				log.info("-------------- Processing a total of " + list.size() + " notifications ----------------");
			}

			for (NotificationModel item : list) {

				log.info("Sending notification with id [{}] through {} channel", item.getNotificationId(),
						item.isNotifyEmail() ? "email" : (item.isNotifySms() ? "sms" : "whatsapp"));

				// Send email
				if (item.isNotifyEmail()) {

					List<File> files = null;

					if (item.getAttachements() != null && !item.getAttachements().isEmpty()) {

						files = new ArrayList<File>();

						for (String path : item.getAttachements()) {
							File file = new File(path);

							if (file.exists()) {
								files.add(file);
							}

						}

					}

					mailHelper.sendMessage(item.getRecipient(), item.getSubject(), "", item.getText(), files,
							item.getNotificationId());
				} else if (item.isNotifySms()) {
					// Send sms

//					SendSMSRequest sendSMSRequest = new SendSMSRequest();
//
//					sendSMSRequest.setId(item.getMobile());
//					sendSMSRequest.setTemplate(item.getSubject());
//
//					String body = item.getText();
//
//					JSONArray params = new JSONArray(body);
//
//					for (int i = 0; i < params.length(); i++) {
//
//						JSONObject param = params.getJSONObject(i);
//
//						if (param.has("lang")) {
//							sendSMSRequest.setSmsLanguage(param.getString("lang").toUpperCase().substring(0, 1));
//							continue;
//						}
//
//						Parameter parameter = new Parameter();
//
//						parameter.setValue(param.getString("value"));
//						parameter.setIsEncrypted(param.getBoolean("isEncrypted"));
//
//						sendSMSRequest.getParameter().add(parameter);
//					}
//
//					AbstractWebserviceResponse<SendSMSResponse> sendSMSResponse = sendSmsRequestCall
//							.callService(sendSMSRequest);
//
//					iWsService.logWebServiceRequest(new WsLogRequest<SendSMSRequest, SendSMSResponse>("SEND_SMS",
//							sendSMSResponse.isSuccess(), sendSMSRequest, sendSMSResponse.getResponse()));
//
//					if (sendSMSResponse.getResponse().getFault() != null) {
//						log.error("Error sending sms to [{}] with error [{}]", item.getMobile(),
//								sendSMSResponse.getResponse().getFault().getDescription());
//						notificationLogDao.updateEmailSms(item.getId(), false,
//								sendSMSResponse.getResponse().getFault().getDescription());
//					} else {
//						log.debug("Successfully sending message [{}] to [{}]",
//								sendSMSResponse.getResponse().getResponse(), item.getMobile());
//						notificationLogDao.updateEmailSms(item.getId(), true, null);
//					}

				} else if (item.isNotifyWhatsapp()) {

					try {
						String to = item.getCallingCode() + item.getMobileNumber();

						whatsappSenderService.sendOtp(to, item.getText());

						notificationDao.updateNotificationWhatsapp(item.getNotificationId(), true, null);

					} catch (Exception e) {
						notificationDao.updateNotificationWhatsapp(item.getNotificationId(), false, e.getMessage());
					}
				}

			}

		} catch (Exception e) {
			log.error("Failed to {}, error", fnSendNotification, e);
		}

	}

}
