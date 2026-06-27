package com.wasselni.notification.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wasselni.notification.server.model.Version;

@SpringBootApplication(scanBasePackages = "com.wasselni")
public class NotificationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServerApplication.class, args);
		Version.version(args);
	}
}
