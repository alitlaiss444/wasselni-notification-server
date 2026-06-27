package com.wasselni.common.utils.map.xml.cs.marsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.wasselni.common.utils.map.xml.cs.DefaultsFrom;
import com.wasselni.common.utils.map.xml.cs.DefaultsTo;
import com.wasselni.common.utils.map.xml.cs.Map;
import com.wasselni.common.utils.map.xml.cs.Mapping;
import com.wasselni.common.utils.map.xml.cs.Mappings;
import com.wasselni.common.utils.map.xml.cs.Maps;
import com.wasselni.common.utils.map.xml.cs.ValuesFrom;
import com.wasselni.common.utils.map.xml.cs.ValuesTo;

public class GenericXmlMapMarshaller {

	private static final Log log = LogFactory.getLog(GenericXmlMapMarshaller.class);

	/**
	 * 
	 * @param xmlRsp
	 * @return
	 */
	public static Maps getMapsObject(String xml) {

		try {

			Maps m = new Maps();
			XStream xstream = new XStream();
			xstream.alias("Maps", Maps.class);
			xstream.alias("Map", Map.class);
			xstream.addImplicitCollection(Maps.class, "Map", "Map", Map.class);

			xstream.alias("Mappings", Mappings.class);
			xstream.alias("DefaultsFrom", DefaultsFrom.class);
			xstream.addImplicitCollection(DefaultsFrom.class, "Value", String.class);

			xstream.alias("DefaultsTo", DefaultsTo.class);
			xstream.addImplicitCollection(DefaultsTo.class, "Value", String.class);

			xstream.alias("Mapping", Mapping.class);
			xstream.addImplicitCollection(Mappings.class, "Mapping", Mapping.class);
			xstream.alias("ValuesFrom", ValuesFrom.class);
			xstream.addImplicitCollection(ValuesFrom.class, "Value", String.class);
			xstream.alias("ValuesTo", ValuesTo.class);
			xstream.addImplicitCollection(ValuesTo.class, "Value", String.class);

			m = (Maps) xstream.fromXML(xml);

			return m;

		} catch (Exception e) {
			log.error("Failed parsing maps [" + e.getMessage() + "]", e);
			return null;
		}
	}
}
