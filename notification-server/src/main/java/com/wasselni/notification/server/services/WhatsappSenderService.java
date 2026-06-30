package com.wasselni.notification.server.services;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import com.wasselni.notification.server.dao.notification.INotificationDao;

@Service
public class WhatsappSenderService {

	private static final Logger log = LogManager.getLogger(WhatsappSenderService.class);

	private @Value("${notification.server.whatsapp.api-version}") String apiVersion;
	private @Value("${notification.server.whatsapp.phone-number-id}") String phoneNumberId;
	private @Value("${notification.server.whatsapp.access-token}") String accessToken;

	private @Autowired INotificationDao notificationDao;
	private @Autowired RestClient restClient;

	public void sendOtp(Long notificationId, String to, String otp) {
		sendTemplateMessage(notificationId, to, "wasselni_notify", "en", List.of(otp));
	}

	public void sendTemplateMessage(Long notificationId, String to, String templateName, String languageCode,
			List<String> parameters) {

		List<Map<String, Object>> bodyParams = parameters.stream()
				.map(value -> Map.<String, Object>of("type", "text", "text", value)).toList();

		Map<String, Object> requestBody = Map.of("messaging_product", "whatsapp", "to", to, "type", "template",
				"template", Map.of("name", templateName, "language", Map.of("code", languageCode), "components",
						List.of(Map.of("type", "body", "parameters", bodyParams))));

		String url = "/" + apiVersion + "/" + phoneNumberId + "/messages";

		log.info("WhatsApp Request URL: {}", url);
		log.info("WhatsApp Request Body: {}", requestBody);

		try {

			String response = restClient.post().uri("/{version}/{phoneNumberId}/messages", apiVersion, phoneNumberId)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
					.body(requestBody).retrieve().body(String.class);

			log.info("WhatsApp Response: {}", response);

			notificationDao.updateNotificationWhatsapp(notificationId, true, null);

		} catch (HttpStatusCodeException e) {

			log.error("WhatsApp API Error [{}]: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);

			notificationDao.updateNotificationWhatsapp(notificationId, false, e.getMessage());

		} catch (Exception e) {

			log.error("Failed to send WhatsApp message.", e);

			notificationDao.updateNotificationWhatsapp(notificationId, false, e.getMessage());
		}
	}

}