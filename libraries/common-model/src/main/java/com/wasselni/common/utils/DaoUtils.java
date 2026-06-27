
package com.wasselni.common.utils;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class DaoUtils {

	public static <T> String arrayToInQueryString(List<T> items) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < items.size(); i++) {
			sb.append("?,");
		}

		String finalString = sb.toString();

		finalString = finalString.substring(0, finalString.length() - 1);

		return finalString.toString();

	}

	public static <T> String arrayToInQueryString(String query, List<T> items) {

		if (items == null || items.isEmpty())
			return "";

		StringBuilder initialQuery = new StringBuilder();
		initialQuery.append(" AND (" + query);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < items.size(); i++) {

			if (i > 0 && i % 999 == 0) {
				initialQuery = new StringBuilder(String.format(initialQuery.toString(), sb.toString()));
				initialQuery.append(" \n OR " + query);
				sb = new StringBuilder();
			}

			sb.append("?,");
		}

		initialQuery = new StringBuilder(String.format(initialQuery.toString(), sb.toString()));

		String finalString = initialQuery.toString();

		finalString += ")";

		finalString = finalString.replace(",)", ")");

		return finalString.toString();

	}

	public static <T> Object[] arrayToInQueryValue(List<T> items, Object[] params) {

		for (int i = 0; i < items.size(); i++) {
			params = ArrayUtils.add(params, items.get(i));
		}

		return params;

	}

}
