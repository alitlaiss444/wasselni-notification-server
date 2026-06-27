package com.wasselni.common.utils.format;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.wasselni.common.utils.format.xml.AllowedValues;
import com.wasselni.common.utils.format.xml.FormatFile;
import com.wasselni.common.utils.format.xml.FormatLine;
import com.wasselni.common.utils.format.xml.UniqueKey;
import com.wasselni.common.utils.format.xml.Variable;

public class FormatFileMarshaller {

	private static final Log log = LogFactory.getLog(FormatFileMarshaller.class);

	/**
	 * 
	 * @param xmlRsp
	 * @return
	 */
	public static FormatFile getConfigObject(String xml) {

		try {

			FormatFile i = new FormatFile();

			i = (FormatFile) getXstream().fromXML(xml);

			return i;

		} catch (Exception e) {
			log.error("Failed parsing Configuration [" + e.getMessage() + "]", e);
			return null;
		}
	}

	public static String unmarshallConfig(FormatFile formatFile) {
		try {
			String retXml = "";
			retXml = getXstream().toXML(formatFile);
			return (retXml.replace("__", "_")); // xtream bug

		} catch (Exception ex) {
			log.error("Failed generating xml for response: " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Gets an xstream object to be used for marshalling/unmarshalling
	 * 
	 * @return
	 */
	private static XStream getXstream() {

		XStream xstream = new XStream();
		xstream.alias("FormatFile", FormatFile.class);
		xstream.alias("FormatLine", FormatLine.class);
		xstream.alias("Header", FormatLine.class);
		xstream.alias("Footer", FormatLine.class);
		xstream.alias("Variable", Variable.class);
		xstream.alias("UniqueKey", UniqueKey.class);
		xstream.alias("MigrateDb", Boolean.class);
		xstream.addImplicitCollection(FormatLine.class, "Variable", Variable.class);
		xstream.addImplicitCollection(FormatLine.class, "UniqueKey", UniqueKey.class);
		xstream.addImplicitCollection(UniqueKey.class, "Field", String.class);

		xstream.alias("AllowedValues", AllowedValues.class);
		xstream.addImplicitCollection(AllowedValues.class, "Value", String.class);
		xstream.alias("AllowedSeperator", AllowedValues.class);
		return xstream;
	}

}
