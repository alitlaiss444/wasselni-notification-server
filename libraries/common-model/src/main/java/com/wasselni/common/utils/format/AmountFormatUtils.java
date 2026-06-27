package com.wasselni.common.utils.format;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Format utilities for amounts/Big Decimals
 *
 */
public class AmountFormatUtils {

	public static String amountToNoDecimalPointFixed(BigDecimal amount, int dp) {

		while (dp > 0) {
			amount = amount.multiply(new BigDecimal(10));
			dp--;
		}
		return String.format("%.0f", amount.setScale(0, RoundingMode.DOWN));
	}

	/**
	 * Converts a decimal number to an amount with no decimal point
	 * 
	 * @param amount amount to convert
	 * @param dp     number of decimal points to consider in the formatting
	 * @return formatted amount with no decimal point after multiplying it with 10
	 *         times the number of decimal points specified. The output value is
	 *         rounded down
	 */
	public static String amountToNoDecimalPoint(BigDecimal amount, int dp) {

		while (dp > 0) {
			amount.multiply(new BigDecimal(10));
			dp--;
		}
		return String.format("%.0f", amount.setScale(0, RoundingMode.DOWN));
	}

	/**
	 * Converts the decimal number to a string with exactly the number of decimal
	 * points specified
	 * 
	 * @param amount amount to format
	 * @param dp     the number of decimal points that the output should have
	 * @return formatted amount with exactly the number of decimal points specified
	 */
	public static String formatAmountDecimalPlaces(BigDecimal amount, int dp) {

		amount = amount.setScale(dp, RoundingMode.HALF_UP);
		return amount.toPlainString();
	}

	/**
	 * Converts the decimal number to a string with exactly the number of decimal
	 * points specified and stripping trailing zeros when possible
	 * 
	 * @param amount amount to format
	 * @param dp     the number of decimal points that the output should have
	 * @return formatted amount with exactly the number of decimal points specified
	 */
	public static String formatAmountDecimalPlacesNoTrailingZeros(BigDecimal amount, int dp) {

		amount = amount.setScale(dp, RoundingMode.HALF_UP);
		return amount.stripTrailingZeros().toPlainString();
	}

	/**
	 * Rounds an amount based on the dp passed to it. If not specified, 2 is used by
	 * default
	 * 
	 * @param amount amount to round
	 * @param dp     decimal places to use for the rounding
	 * @return rounded amount
	 */
	public static BigDecimal roundAmount(BigDecimal amount, Short dp) {

		short decimalPlaces = 2;

		if (dp != null) {
			decimalPlaces = dp.shortValue();
		}

		if (decimalPlaces < 0) {
			decimalPlaces = 2;
		}

		return amount.setScale(decimalPlaces, RoundingMode.HALF_UP);
	}

}
