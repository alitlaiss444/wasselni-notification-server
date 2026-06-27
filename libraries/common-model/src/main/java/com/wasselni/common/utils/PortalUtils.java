
package com.wasselni.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PortalUtils {

	public static String arrayToBinaryString(Integer[] arr) {
		int binaryValue = 0;
		for (int num : arr) {
			binaryValue |= (1 << (num));
		}
		String binaryStr = String.format("%7s", Integer.toBinaryString(binaryValue)).replace(" ", "0");
		return new StringBuilder(binaryStr).reverse().toString();
	}

	public static Integer[] binaryToArray(String binaryStr) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < binaryStr.length(); i++) {
			if (binaryStr.charAt(i) == '1') {
				result.add(i);
			}
		}
		return result.toArray(new Integer[0]);
	}

	public static String convertLongToTime(long timeInt) {
		long hours = timeInt / 10000;
		long minutes = (timeInt / 100) % 100;

		return String.format("%02d:%02d", hours, minutes);

	}

	public static long convertTimeToLong(String timeStr) {
		String[] parts = timeStr.split(":");
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		return ((hours * 100) + minutes) * 100;
	}

	public String convertPanDisplay(String panDisplay) {
		if (StringUtils.isBlank(panDisplay))
			return panDisplay;

		return panDisplay.replace("_", "******");
	}

	public String maskLast4Digit(String pan) {
		if (pan.length() > 4) {
			return "****" + pan.substring(pan.length() - 4);
		} else {
			return pan;
		}
	}

	public String maskPan(String pan, String layout) {
		if (StringUtils.isBlank(pan)) {
			return pan;
		}

		if (pan.contains("_") || pan.contains("X")) {
			return pan;
		}

		if (pan.length() < 16) {
			return pan;
		}

		if (StringUtils.isBlank(layout)) {
			layout = "_";
		}

		String panDisplay = pan.substring(0, 6) + layout + pan.substring(pan.length() - 4);

		return panDisplay;
	}

	public String saveFile(String orgFileName, String newFileName, String base64Document, String path) {

		if (StringUtils.isBlank(orgFileName) || StringUtils.isBlank(base64Document) || StringUtils.isBlank(newFileName)
				|| StringUtils.isBlank(path)) {
			return null;
		}

		String directoryPath = path + "/" + DateUtils.getSerialDate();

		String filePath = directoryPath + "/" + newFileName + "." + FilenameUtils.getExtension(orgFileName);

		File directory = new File(directoryPath);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		byte[] data = Base64.decodeBase64(base64Document);

		OutputStream outputStream = null;
		try {

			InputStream inputStream = new ByteArrayInputStream(data);

			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);

			File targetFile = new File(filePath);
			outputStream = new FileOutputStream(targetFile);
			outputStream.write(buffer);

		} catch (IOException e) {
			return null;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			outputStream = null;
		}

		return filePath;

	}

	/**
	 * Calculate password complexity
	 * 
	 * @param password
	 * @return
	 */
	public int calculatePasswordStrength(String password) {

		// total score of password
		int iPasswordScore = 0;

		if (password.length() < 8)
			return 0;
		else if (password.length() >= 10)
			iPasswordScore += 2;
		else
			iPasswordScore += 1;

		// if it contains one digit, add 2 to total score
		if (password.matches("(?=.*[0-9]).*"))
			iPasswordScore += 2;

		// if it contains one lower case letter, add 2 to total score
		if (password.matches("(?=.*[a-z]).*"))
			iPasswordScore += 2;

		// if it contains one upper case letter, add 2 to total score
		if (password.matches("(?=.*[A-Z]).*"))
			iPasswordScore += 2;

		// if it contains one special character, add 2 to total score
		if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
			iPasswordScore += 2;

		return iPasswordScore;

	}

	public boolean validateImage(String str) {

		if (StringUtils.isEmpty(str)) {
			return false;
		}

		String ext = FilenameUtils.getExtension(str);

		return ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("png");

	}

	public boolean validateInvoice(String str) {

		if (StringUtils.isEmpty(str)) {
			return false;
		}

		String ext = FilenameUtils.getExtension(str);

		return ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("png")
				|| ext.toLowerCase().equals("pdf");

	}

	public boolean isValidBase64(String input) {
		try {
			byte[] decodedBytes = Base64.decodeBase64(input);
			String reencodedString = Base64.encodeBase64String(decodedBytes);
			return input.equals(reencodedString);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public float validateBase64Size(String base64) {

		if (StringUtils.isEmpty(base64)) {
			return -1;
		}

		int y = base64.endsWith("==") ? 2 : 1;

		return ((float) (base64.length() * 0.75)) - y;

	}

	public static boolean isNull(Long id) {

		if (id == null || id == 0)
			return true;

		return false;

	}

	public static Long isNull(Long id, Long defaultVal) {

		if (id == null || id < 0)
			return defaultVal;

		return id;

	}

	public static boolean isNull(Short id) {

		if (id == null || id == 0)
			return true;

		return false;

	}

	public static Short isNull(Short id, Short defaultVal) {

		if (id == null || id == 0)
			return defaultVal;

		return id;

	}

	public static boolean isNull(BigDecimal amount) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
			return true;

		return false;

	}

	public static BigDecimal isNull(BigDecimal amount, BigDecimal defaultVal) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
			return defaultVal;

		return amount;

	}

	public static String formatAmount(BigDecimal amount, String currency, int scale) {

		if (amount == null) {
			amount = BigDecimal.ZERO;
		}

		return currency + " " + String.format("%,." + scale + "f", amount.setScale(scale, RoundingMode.DOWN));
	}

	public String removeCallingCodeFromMobile(String callingCode, String mobile) {
		return mobile.replaceFirst(callingCode, "");
	}

	public String cleanBase64Format(String base64) {
		return base64.substring(base64.indexOf(",")).replace("\\", "");
	}

	public String generateFileName(String name, int counter) {
		return FilenameUtils.getBaseName(name) + "-" + StringUtils.leftPad(String.valueOf(counter), 2, "0") + "-"
				+ DateUtils.getCurrentTimeAsLong();
	}

	/**
	 * Generates a random alpha-numeric[A-Z/0-9] string with the specified length
	 * 
	 * @param len length of the random string to generate
	 * @return random alphanumeric as string
	 */
	public static String generateRandomAlphaNumeric(int len) {

		String ran = "";
		SecureRandom random = new SecureRandom();
		String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < len; i++) {
			ran += characters.charAt(random.nextInt(characters.length()));
		}

		return ran;
	}

	public static String generateTemporaryToken(String ipAddress, String userId, String function) {

		String token = ipAddress + "|" + userId + "|" + function + "|" + System.currentTimeMillis();

		return AESUtils.encrypt(token);

	}

	public static boolean validateTemporaryToken(String ipAddress, String userId, String token, String function) {

		try {

			String decodedToken = AESUtils.decrypt(token);

			System.out.println("decoded Token : " + decodedToken);

			String[] splitedToken = decodedToken.split("\\|");

			Long tokenGenDateLong = Long.parseLong(splitedToken[3]);

			Date tokenGenDate = new Date(tokenGenDateLong);

			System.out.println("Date Token : " + tokenGenDate);

			System.out.println("Time diff : " + DateUtils.getDifferenceInMinutes(tokenGenDate, new Date()));

			return splitedToken[0].equals(ipAddress) && splitedToken[1].equals(userId)
					&& splitedToken[2].equals(function)
					&& DateUtils.getDifferenceInMinutes(tokenGenDate, new Date()) < 30;

		} catch (Exception e) {
			return false;
		}

	}

	public static String getShortCifFromCif(String cifNumber) {

		if (StringUtils.isBlank(cifNumber) || cifNumber.length() < 8)
			return cifNumber;

		return cifNumber.substring(0, 8);
	}

	/**
	 * Extracts the first two characters of the bank identifier from a Saudi IBAN.
	 * Expected Saudi IBAN format: SAkkbbbbcccccccccccccccc Where 'bbbb' is the
	 * 4-character bank identifier, this method returns 'bb'.
	 *
	 * @param iban the full Saudi IBAN string
	 * @return the first two characters of the bank identifier, or null if invalid
	 */
	public static String extractBankShortCode(String iban) {
		if (iban == null || !iban.startsWith("SA") || iban.length() < 8) {
			return null;
		}

		String fullBankIdentifier = iban.substring(4, 8); // Extract 'bbbb'
		String shortBankCode = fullBankIdentifier.substring(0, 2); // Get 'bb'

		return shortBankCode;
	}

}
