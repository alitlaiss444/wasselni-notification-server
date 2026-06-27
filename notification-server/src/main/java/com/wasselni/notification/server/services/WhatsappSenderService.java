package com.wasselni.notification.server.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WhatsappSenderService {

	private @Value("${notification.server.whatsapp.api-version}") String apiVersion;
	private @Value("${notification.server.whatsapp.phone-number-id}") String phoneNumberId;
	private @Value("${notification.server.whatsapp.access-token}") String accessToken;

	private @Autowired RestClient restClient;

	public String sendTemplateMessage(String to, String templateName, String languageCode, List<String> parameters) {

		List<Map<String, Object>> bodyParams = parameters.stream()
				.map(value -> Map.<String, Object>of("type", "text", "text", value)).toList();

		Map<String, Object> requestBody = Map.of("messaging_product", "whatsapp", "to", to, "type", "template",
				"template", Map.of("name", templateName, "language", Map.of("code", languageCode), "components",
						List.of(Map.of("type", "body", "parameters", bodyParams))));

		return restClient.post().uri("/{version}/{phoneNumberId}/messages", apiVersion, phoneNumberId)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
				.body(requestBody).retrieve().body(String.class);
	}

	public String sendOtp(String to, String otp) {
		return sendTemplateMessage(to, "jaspers_market_order_confirmation_v1", "en_US",
				List.of("Ali Tleiss and welcome to Wasselni Services", otp, "Jun 26, 2026"));
	}
}