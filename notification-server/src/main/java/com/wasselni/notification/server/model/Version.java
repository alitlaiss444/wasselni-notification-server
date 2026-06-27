package com.wasselni.notification.server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Version {

	private static final String VERSION = "Base - build 0.0.1-WASSELNI-RELEASE";
	private static final String APP_NAME = "Notification Server";
	private static final String MIN_JAVA_VERSION = "21*";

	private static final Logger log = LogManager.getLogger(Version.class);

	public static final String[] PATCHES = new String[] { VERSION, "build 0.0.1  (01062026.01)- 	First RELEASE.", };

	private static void displayVersion() {
		log.info(PRODUCT_NAME);
		int max = getMaxLength(PATCHES);
		dash(max);
		log.info(APP_NAME + " - " + VERSION);
		log.info("Built for Java(TM) SE Runtime Environment " + MIN_JAVA_VERSION + " & above.");
		log.info("Using Runtime java version: " + System.getProperty("java.version"));
		dash(max);
		displayPatches();
		dash(max);
	}

	private static void displayPatches() {
		for (String patch : PATCHES) {
			log.info(patch);
		}
	}

	public static void version(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("-v")) {
			displayVersion();
			System.exit(0);
		} else {
			displayVersion();
		}
	}

	private static int getMaxLength(String[] objects) {
		int max = 0;
		for (String version : objects) {
			if (version.length() > max) {
				max = version.length();
			}
		}
		return max;
	}

	private static void dash(int length) {
		String dashes = "";
		for (int i = 0; i < length + 5; i++) {
			dashes += "-";
		}
		log.info(dashes);
	}

	private static final String PRODUCT_NAME = //
			"\n _   _  ____ _______ _____ ______ _____ _____       _______ _____ ____  _   _      _____ ______ _______      ________ _____              \r\n"
					+ "| \\ | |/ __ \\__   __|_   _|  ____|_   _/ ____|   /\\|__   __|_   _/ __ \\| \\ | |    / ____|  ____|  __ \\ \\    / /  ____|  __ \\     \r\n"
					+ "|  \\| | |  | | | |    | | | |__    | || |       /  \\  | |    | || |  | |  \\| |   | (___ | |__  | |__) \\ \\  / /| |__  | |__) |       \r\n"
					+ "| . ` | |  | | | |    | | |  __|   | || |      / /\\ \\ | |    | || |  | | . ` |    \\___ \\|  __| |  _  / \\ \\/ / |  __| |  _  /       \r\n"
					+ "| |\\  | |__| | | |   _| |_| |     _| || |____ / ____ \\| |   _| || |__| | |\\  |    ____) | |____| | \\ \\  \\  /  | |____| | \\ \\     \r\n"
					+ "|_| \\_|\\____/  |_|  |_____|_|    |_____\\_____/_/    \\_\\_|  |_____\\____/|_| \\_|   |_____/|______|_|  \\_\\  \\/   |______|_|  \\_\\\r\n";

}
