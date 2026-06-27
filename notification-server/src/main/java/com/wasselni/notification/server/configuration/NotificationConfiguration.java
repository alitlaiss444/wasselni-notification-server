package com.wasselni.notification.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NotificationConfiguration {

	@Bean
	public RestClient whatsappRestClient() {
		return RestClient.builder().baseUrl("https://graph.facebook.com").build();
	}

}
