package com.wasselni.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Utility functions to mask sensitive data (mainly pans)
 *
 */
public class PciUtils {

	// masking Char
	public static String pciMaskPattern = "";

	private static PciPatterns patterns = new PciPatterns();

	/**
	 * Generates a string with the masking char of with the length specified
	 * 
	 * @param l length of the string to generate
	 * @return string with the length specified all masking char
	 */
	public static String doChar(int l) {

		String r = "";

		for (int i = 0; i < l; i++) {
			r += (patterns == null) ? "*" : patterns.getMaskingChar();
		}
		return r;
	}

	/**
	 * Masks an input string using the masking char (default *)
	 * 
	 * @param input value to mask
	 * @return masked value
	 */
	public static String mask(String input) {

		String cardMask;
		boolean ok = false;

		if (StringUtils.isBlank(input))
			return "";

		StringBuilder card = new StringBuilder(input.replace(" ", "")); // copy

		if (card.length() < 10) {
			return doChar(card.length());
		}

		for (int i = 0; i < patterns.getPciMaskPattern().length; i++) {
			pciMaskPattern = patterns.getPciMaskPattern()[i];
			if (pciMaskPattern.length() == card.length()) {
				ok = true;
				for (int j = 0; j < pciMaskPattern.length(); j++) {
					if (pciMaskPattern.charAt(j) == '*') {
						card.setCharAt(j, patterns.getMaskingChar().charAt(0));
					}
				}
				break;
			} else {
				continue;
			}
		}
		cardMask = (ok) ? card.toString() : defaultMask(input);
		return cardMask;
	}

	/**
	 * Masks an input using the default mask first 6 and last 4 clear
	 * 
	 * @param input data to mask
	 * @return masked output. In case the input is empty, 'n/a' is returned. In case
	 *         the length of the input is less than 10, the full input will be
	 *         masked
	 */
	public static String defaultMask(String input) {

		int first = 6;
		int last = 4;

		if (input == null) {
			return "n/a";
		} else if (input.equalsIgnoreCase("")) {
			return "n/a";
		}

		String card = new String(input.replace(" ", "")); // copy

		if (card.length() < 10) {
			return doChar(card.length());
		}

		int diff = card.length() - (first + last);

		String firstX = card.substring(0, first);
		String lastX = card.substring(card.length() - last, card.length());

		return firstX + doChar(diff) + lastX;

	}
}

class PciPatterns {

	private String[] pciMaskPattern = new String[] {};
	private String maskingChar = "*"; // default

	public String[] getPciMaskPattern() {
		return pciMaskPattern;
	}

	public void setPciMaskPattern(String[] pciMaskPattern) {
		this.pciMaskPattern = pciMaskPattern;
	}

	public String getMaskingChar() {
		return maskingChar;
	}

	public void setMaskingChar(String maskingChar) {
		this.maskingChar = maskingChar;
	}

}
