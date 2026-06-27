package com.wasselni.notification.server.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptUtils {

	private static String DEFAULT_PASSWORD = "notyourbusiness";

	public static String defaultDecrypt(String encrypted) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(DEFAULT_PASSWORD);
		return textEncryptor.decrypt(encrypted);
	}

	public static String defaultEncrypt(String clear) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(DEFAULT_PASSWORD);
		return textEncryptor.encrypt(clear);
	}

	public static String hashSHA256(String msg) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(msg.getBytes());
		byte[] outputDigest = messageDigest.digest();
		return new String(Base64.encodeBase64(outputDigest));
	}

}
