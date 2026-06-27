package com.wasselni.common.utils;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.expections.BackendException;

public class DataValidationUtil {

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotValid(String value, int minLength, int maxLength) {

		if (isTrimmedEmpty(value) && minLength > 0) {
			return false;
		}

		if (value.length() > maxLength || value.length() < minLength) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isTrimmedEmpty(String value) {
		if (value == null) {
			return true;
		}

		if (value.trim().length() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isNull(Long id) {

		if (id == null || id == 0)
			return true;

		return false;

	}

	public static boolean isNullAllowZero(Long id) {

		if (id == null || id < 0)
			return true;

		return false;

	}

	public static Long isNull(Long id, Long defaultVal) {

		if (id == null || id < 0)
			return defaultVal;

		return id;

	}

	public static Long validateLong(Long value) {

		if (value == null || value < 0)
			return null;

		return value;

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
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

	public static boolean isNullAllowZero(BigDecimal amount) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
			return true;

		return false;

	}

	public static BigDecimal isNull(BigDecimal amount, BigDecimal defaultVal) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
			return defaultVal;

		return amount;

	}

	public static BigDecimal validateAmount(BigDecimal amount) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
			return null;

		return amount;

	}

	public static boolean isNull(Double amount) {

		if (amount == null || amount == 0)
			return true;

		return false;

	}

	/**
	 * Validate Email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) throws BackendException {
		EmailValidator validator = EmailValidator.getInstance();

		if (!validator.isValid(email)) {
			throw new BackendException(SystemError.VE_VALUE_MAIL_NOT_IN_FORMAT, email);
		}

		return true;
	}

	/**
	 * Calculate password complexity
	 * 
	 * @param password
	 * @return
	 */
	public static int calculatePasswordStrength(String password) {

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

	public static Integer countStringOccurence(String mainString, String searchString) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {

			lastIndex = mainString.indexOf(searchString, lastIndex);

			if (lastIndex != -1) {
				count++;
				lastIndex += searchString.length();
			}
		}

		return count;
	}

	public static String convertDisplayToMaskedPan(String displayPan) {

		String maskedPan = "";

		if (displayPan.contains("_")) {
			maskedPan = displayPan.replace("_", "******");
		}

		return maskedPan;

	}

	public static Boolean isNumeric(String value) {

		if (StringUtils.isBlank(value)) {
			return false;
		}

		Pattern pattern = Pattern.compile("^\\d+$");

		return pattern.matcher(value).matches();

	}

	/**
	 * 
	 * @param mobile
	 * @param description used while throwing error Mobile/Telephone
	 * @return
	 * @throws BackendException
	 * @throws NumberParseException
	 */
	public static boolean isValidMobile(String mobile, String description)
			throws BackendException, NumberParseException {

		if (!isNumeric(mobile)) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		if (mobile.length() > 15 || mobile.length() < 7) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		return true;
	}

	/**
	 * 
	 * @param mobile
	 * @param country     isocode Alpha2
	 * @param description used while throwing error Mobile/Telephone
	 * @return
	 * @throws BackendException
	 * @throws NumberParseException
	 */
	public static boolean isValidMobile(String mobile, String country, String description)
			throws BackendException, NumberParseException {

		if (!isNumeric(mobile)) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		if (mobile.length() > 15 || mobile.length() < 7) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		if (country != null) {
			if (country.toUpperCase().equals("SA") && mobile.startsWith("0")) {
				throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
			}
		}

		if (country != null
				&& !PhoneNumberUtil.getInstance().isValidNumber(PhoneNumberUtil.getInstance().parse(mobile, country))) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		return true;
	}

	/**
	 * 
	 * @param mobile
	 * @param countryIso  isocode
	 * @param description used while throwing error Mobile/Telephone
	 * @return
	 * @throws BackendException
	 * @throws NumberParseException
	 */
	public static boolean isValidMobile(String mobile, Short countryIso, String description)
			throws BackendException, NumberParseException {

		if (!isNumeric(mobile)) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		if (mobile.length() > 15 || mobile.length() < 7) {
			throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
		}

		if (countryIso != null) {

			String countryCode = PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(countryIso);

			if (countryCode.equals("SA") && mobile.startsWith("0")) {
				throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
			}

			Locale locale = new Locale("", countryCode);

			if (!PhoneNumberUtil.getInstance()
					.isValidNumber(PhoneNumberUtil.getInstance().parse(mobile, locale.getCountry()))) {
				throw new BackendException(SystemError.VE_VALUE_MOBILE_NOT_IN_FORMAT, description, mobile);
			}
		}

		return true;
	}

	public static String maskPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() <= 6) {
			return phoneNumber; // Phone number is too short to mask
		}

		String firstThreeDigits = phoneNumber.substring(0, 3);
		String lastThreeDigits = phoneNumber.substring(phoneNumber.length() - 3);

		StringBuilder maskedNumber = new StringBuilder();
		maskedNumber.append(firstThreeDigits);
		for (int i = 3; i < phoneNumber.length() - 3; i++) {
			maskedNumber.append("*");
		}
		maskedNumber.append(lastThreeDigits);

		return maskedNumber.toString();
	}

}
