
package com.wasselni.common.utils;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

	private static String secretKey = "BAJ_AES_06102024";

	public static String encrypt(String strToEncrypt) {
		try {
			Key aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());
			return new String(Base64.getEncoder().encode(encrypted));
		} catch (Exception e) {
			return null;
		}
	}

	public static String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			Key aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			return null;
		}
	}

}
