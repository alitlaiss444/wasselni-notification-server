
package com.wasselni.common.model.common;

public class DefaultValidationPattern {

	public final static String ALPHA_NUMERIC_WITH_SPACE = "^[A-Za-z0-9 ]*$";
	public final static String ALPHA_NUMERIC = "^[A-Za-z0-9]*$";
	public final static String ALPHA = "^[A-Za-z]*$";
	public final static String ALPHA_NUMERIC_WITH_DASH = "^[A-Za-z0-9-]*$";
	public final static String ALPHA_WITH_SPACE = "^[A-Za-z ]*$";
	public final static String ALPHA_NUMERIC_WITH_SYMBOLS = "^[0-9a-zA-Z #@\\.\\-/]*$";
	public final static String ALPHA_INTERNAL_CODES = "^[a-zA-Z_-]*$";
	public final static String NUMERIC = "^[0-9]*$";
	public final static String EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	public final static String UUID = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
	public final static String URL = "^([-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&=]*))*$";
	public final static String ALPHA_NUMERIC_WITH_SYMBOLS_ALL = "^[0-9a-zA-Z .^@_$-]*$";
	public final static String ALPHA_NUMERIC_ARABIC_WITH_SPACE = "^[0-9a-zA-Z \\u0621-\\u064A]*$";
	public final static String ID_NUMBER = "^[1-2][0-9]{9}$";
	public final static String DATE_GRE_HIJRI = "^(0[123456789]|1[012])-(12|13|14|15|19|20)\\d\\d$";

}
