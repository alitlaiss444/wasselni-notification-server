package com.wasselni.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

/**
 * Database related utility functions
 * 
 * @author Wassim
 *
 */
public class DatabaseUtils {

	private static final Log log = LogFactory.getLog(DatabaseUtils.class);

	/**
	 * Converts an object either CLOB or string to string
	 * 
	 * @param clob clob object which also can be a string
	 * @return clob converted to string
	 * @throws SystemException
	 */
	public static String clobToString(Object clob) throws SystemException {

		return clobToString(clob, true);
	}

	/**
	 * Converts an object either CLOB or string to string
	 * 
	 * @param clob           clob object which also can be a string
	 * @param keepEndOflines specifies if end of lines should be kept
	 * @return clob converted to string
	 * @throws SystemException
	 */
	public static String clobToString(Object clob, boolean keepEndOflines) throws SystemException {
		if (clob == null)
			return "";

		if (clob instanceof String)
			return String.valueOf(clob);

		return clobToString((Clob) clob, keepEndOflines);
	}

	/**
	 * Converts a CLOB to String
	 * 
	 * @param clb Clob to convert to string
	 * @return the converted CLOB to string
	 * @throws SystemException
	 */
	public static String clobToString(Clob clb) throws SystemException {
		return clobToString(clb, true);
	}

	/**
	 * Converts a CLOB to String
	 * 
	 * @param clb            Clob to convert to string
	 * @param keepEndOfLines specifies if end of lines should be kept
	 * @return the converted CLOB to string
	 * @throws SystemException
	 */
	public static String clobToString(Clob clb, boolean keepEndOfLines) throws SystemException {
		if (clb == null)
			return "";

		StringBuffer str = new StringBuffer();
		String strng;

		BufferedReader bufferRead;
		try {
			bufferRead = new BufferedReader(clb.getCharacterStream());
			boolean first = true;
			while ((strng = bufferRead.readLine()) != null) {
				if (!first && keepEndOfLines)
					str.append("\n");
				if (first)
					first = false;
				str.append(strng);
			}
			bufferRead.close();
		} catch (SQLException e) {
			log.error("Failed converting clob to string", e);
			throw new SystemException(SystemError.SE_DATABASE, "CLOB Conversion");
		} catch (IOException e) {
			log.error("Failed convertitng clob to string", e);
			throw new SystemException(SystemError.SE_DATABASE, "CLOB Conversion");
		}

		return str.toString();
	}

	/**
	 * Converts an object either BLOB or string to string
	 * 
	 * @param blob blob object which also can be a string
	 * @return blob converted to string
	 * @throws SystemException
	 */
	public static String blobToString(Object blob) throws SystemException {

		if (blob == null)
			return null;

		if (blob instanceof String)
			return String.valueOf(blob);

		if (blob instanceof byte[]) {
			return new String((byte[]) blob);
		}

		return blobToString((Blob) blob);
	}

	/**
	 * Converts a BLOB to String
	 * 
	 * @param blob Blob to convert to string
	 * @return the converted BLOB to string
	 * @throws SystemException
	 */
	public static String blobToString(Blob blob) throws SystemException {

		if (blob == null)
			return null;

		try {
			return new String(blob.getBytes(1l, (int) blob.length()));

		} catch (SQLException e) {
			log.error("Failed converting blob to string", e);
			throw new SystemException(SystemError.SE_DATABASE, "BLOB Conversion");
		}

	}

	/**
	 * Adds paging setup for the specified query
	 * 
	 * @param originalQuery query to add paging to
	 * @param pageSize      page size
	 * @param pageNumber    page number required
	 * @return query with the paging added to it
	 */
	public static String addPagingToQuery(String originalQuery, long pageSize, long pageNumber) {

		String sql = "Select * from                              \n"
				+ "(                                             \n"
				+ "	Select                                       \n"
				+ "		a.*,                                     \n"
				+ "		rownum r__                               \n"
				+ "	from                                         \n"
				+ " (                                            \n" + originalQuery
				+ " ) a                           \n" + " where rownum < ((" + pageNumber + " *  " + pageSize
				+ ") + 1) \n" + ")                              \n" + "Where r__ >= (((" + pageNumber + "-1) * "
				+ pageSize + ") + 1 )";

		return sql;
	}

	/**
	 * Adds paging setup for the specified query
	 * 
	 * @param originalQuery query to add paging to
	 * @param pageSize      page size
	 * @param startRowNum   row number required
	 * @return query with the paging added to it
	 */
	public static String addPagingToQueryFromStartingRow(String originalQuery, long pageSize, long startRowNum) {

		String sql = "Select * from                              \n"
				+ "(                                             \n"
				+ "	Select                                       \n"
				+ "		a.*,                                     \n"
				+ "		rownum r__                               \n"
				+ "	from                                         \n"
				+ " (                                            \n" + originalQuery
				+ " ) a                           \n" + " where rownum < (" + startRowNum + " + " + pageSize + ") \n"
				+ ")                              \n" + "Where r__ >= (" + startRowNum + ")";

		return sql;
	}

	/**
	 * Adds paging setup for the specified query where only one field is expected in
	 * the return
	 * 
	 * @param originalQuery query to add paging to
	 * @param fieldName     field name required in the response
	 * @param pageSize      page size
	 * @param pageNumber    page number required
	 * @return query with the paging added to it
	 */
	public static String addPagingToQuerySingleField(String originalQuery, String fieldName, long pageSize,
			long pageNumber) {

		String sql = "Select " + fieldName + " from              \n"
				+ "(                                             \n"
				+ "	Select                                       \n"
				+ "		a.*,                                     \n"
				+ "		rownum r__                               \n"
				+ "	from                                         \n"
				+ " (                                            \n" + originalQuery
				+ " ) a                          \n" + " where rownum < ((" + pageNumber + " *  " + pageSize
				+ ") + 1) \n" + ")                               \n" + "Where r__ >= (((" + pageNumber + "-1) * "
				+ pageSize + ") + 1)";

		return sql;
	}

	/**
	 * Converts a list to an in condition
	 * 
	 * @param list           list to convert
	 * @param quotesRequired specifies if quotes are required for individual entries
	 * @return converted string
	 */
	public static String convertListToInCondition(List<?> list, boolean quotesRequired) {

		if (Utils.emptyList(list))
			return "";

		String out = " (";
		boolean first = true;

		for (Object object : list) {

			if (!first)
				out += " , ";

			if (quotesRequired) {
				out += "'";
			}

			out += object;

			if (quotesRequired) {
				out += "'";
			}

			if (first)
				first = false;
		}

		out += ") ";

		return out;
	}

	/**
	 * Converts a comma separated list to an in condition with quotes
	 * 
	 * @param list list to convert
	 * @return converted string
	 */
	public static String convertCommaSeparatedListToInCondition(String list) {

		if (StringUtils.isBlank(list))
			return null;

		String out = " (";
		boolean first = true;

		String[] parts = list.split(",");

		for (String part : parts) {

			if (!first)
				out += " , ";

			out += "'";

			out += part;

			out += "'";

			if (first)
				first = false;
		}

		out += ") ";

		return out;
	}

	/**
	 * Formats a date to be used in an oracle query
	 * 
	 * @param date date to format
	 * @return formatted entry
	 */
	public static String formatDateForQuery(Date date) {

		String format = "yyyyMMdd";

		return "to_date('" + new SimpleDateFormat(format).format(date) + "', '" + format + "')";

	}
}
