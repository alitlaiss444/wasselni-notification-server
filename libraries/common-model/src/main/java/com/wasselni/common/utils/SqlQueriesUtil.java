package com.wasselni.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.wasselni.common.model.common.QueryModel;

/**
 * @author JeanTawk
 *
 */
public class SqlQueriesUtil {

  /**
   * 
   * 
   * @param sql
   * @param parameter
   */
  public static String getFinalSQL(QueryModel model) {
    String newSql = model.getSql();
    for (int i = 0; i < model.getParams().length; i++) {
      Object object = model.getParams()[i];
      newSql = StringUtils.replaceOnce(newSql, "?",
          formatParamValue(object).concat(" "));
    }
    return newSql;
  }

  /**
   * @param theValue
   * @return
   */
  private static String formatParamValue(Object theValue) {
    if (theValue == null) {
      return ("NULL");
    } else if (Date.class.isInstance(theValue)) {
      final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      String fecha = "'".concat(df.format(theValue)).concat("'");
      fecha = "TO_DATE(" + fecha + ", 'dd/MM/yyyy')";
      return fecha;
    } else if (Timestamp.class.isInstance(theValue)) {
      final DateFormat df = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
      final String fechaHora = "'".concat(df.format(theValue)).concat("'");
      return fechaHora;
    } else if (ArrayList.class.isInstance(theValue)) {
      final String lista = theValue.toString();
      final String nuevaLista = lista.replaceAll("\\[", "").replaceAll("\\]",
          "");
      return nuevaLista;
    } else if (String.class.isInstance(theValue)) {
      return "'".concat((String) theValue).concat("'");
    }
    return (theValue.toString());
  }
}