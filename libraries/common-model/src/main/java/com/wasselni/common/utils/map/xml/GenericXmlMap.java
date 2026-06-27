package com.wasselni.common.utils.map.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.utils.FileUtils;
import com.wasselni.common.utils.Utils;
import com.wasselni.common.utils.map.xml.cs.Map;
import com.wasselni.common.utils.map.xml.cs.Mapping;
import com.wasselni.common.utils.map.xml.cs.Maps;
import com.wasselni.common.utils.map.xml.cs.ValuesFrom;
import com.wasselni.common.utils.map.xml.cs.ValuesTo;
import com.wasselni.common.utils.map.xml.cs.marsh.GenericXmlMapMarshaller;

/**
 * 
 * Generic xml map utilities to get mappings from an xml file
 *
 */
public class GenericXmlMap {

	private static final Log log = LogFactory.getLog(GenericXmlMap.class);

	private Maps maps;
	private String inputFile;

	/**
	 * Gets the "to" value for a mapping having only a single input
	 * 
	 * @param mapId map id to check the "from" value in
	 * @param from  from value to get the mapped value for
	 * @return mapped value in case a mapping is found, default value if specified
	 *         and no mapping found, null otherwise
	 */
	public String getMapToValue(String mapId, String from) {

		List<String> temp = new ArrayList<String>();
		temp.add(from);
		List<String> to = getMapValues(mapId, temp, true);

		if (null != to)
			return to.get(0);
		else
			return null;
	}

	/**
	 * Get the value from a mapping for a list of from values
	 * 
	 * @param mapId id of the map to check for a mapping in
	 * @param from  list of from values to get the to values for
	 * @return list of mapped values in case a mapping is found, default value if
	 *         specified and no mapping found, null otherwise
	 */
	public List<String> getMapToValueList(String mapId, List<String> from) {

		return getMapValues(mapId, from, true);

	}

	/**
	 * Get the "from" value from the list of "to" values specified assuming only one
	 * value is used in the "from"
	 * 
	 * @param mapId id of the map to look for the mapping in
	 * @param to    list of "to" values to get the "from" value for
	 * @return mapped value in case a mapping is found, default value if specified
	 *         and no mapping found, null otherwise
	 */
	public String getMapFromValue(String mapId, List<String> to) {

		List<String> from = getMapValues(mapId, to, false);

		if (null != from) {
			return from.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets the "from" value for a mapping having only a single input in the "to"
	 * value
	 * 
	 * @param mapId id of the map to look for the mapping in
	 * @param to    value to get the "from" value for
	 * @return mapped value in case a mapping is found, default value if specified
	 *         and no mapping found, null otherwise
	 */
	public String getMapFromValue(String mapId, String to) {

		List<String> temp = new ArrayList<String>();
		temp.add(to);
		List<String> from = getMapValues(mapId, temp, false);

		if (null != from)
			return from.get(0);
		else
			return null;
	}

	/**
	 * Get the "from" values for a list of "to" values
	 * 
	 * @param mapId id of the map to look for the mapping in
	 * @param to    list of "to" values to get the "from" values for
	 * @return List of mapped value in case a mapping is found, default value if
	 *         specified and no mapping found, null otherwise
	 */
	public List<String> getMapFromValueList(String mapId, List<String> to) {
		return getMapValues(mapId, to, false);
	}

	/**
	 * Get the "to" value for a list of "from" values
	 * 
	 * @param mapId id of the map to look for the mapping in
	 * @param from  list of from values to get the map for
	 * @return mapped value in case a mapping is found, default value if specified
	 *         and no mapping found, null otherwise
	 */
	public String getMapToValue(String mapId, List<String> from) {

		List<String> to = getMapValues(mapId, from, true);

		if (null != to) {
			return to.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets the mapping values for the list of values specified
	 * 
	 * @param mapId id of the map to look for the mapping in
	 * @param value list of values to get the mapping for
	 * @param from  boolean indicating if we are getting the from values or to
	 *              values
	 * @return list of mapped values in case a mapping is found, default value if
	 *         specified and no mapping found, null otherwise
	 */
	public List<String> getMapValues(String mapId, List<String> value, boolean from) {

		if (value == null || Utils.emptyList(value)) {
			log.debug("No from value provided for mapping [" + mapId + "]");
			return null;
		}

		log.debug(mapId + ": Trying to get mapping for " + value);

		if (null == maps || Utils.emptyList(maps.getMap())) {
			log.warn("No maps are defined!");
			return null;
		}

		for (Map map : maps.getMap()) {

			if (StringUtils.isNotBlank(map.getId()) && map.getId().equals(mapId)) {

				log.debug("Found map with id [" + mapId + "]");

				if (null == map.getMappings() || Utils.emptyList(map.getMappings().getMapping())) {
					log.debug("No mapping found under map [" + mapId + "]");

					if (map.getDefaultsTo() != null)
						return map.getDefaultsTo().getValue();
					return null;
				}

				for (Mapping m : map.getMappings().getMapping()) {

					String description = m.getDescription();
					List<String> valuesToMatch = null;

					if (from) {
						ValuesFrom v = m.getValuesFrom();
						if (v != null) {
							valuesToMatch = v.getValue();
						} else {
							valuesToMatch = null;
						}
					} else {
						ValuesTo v = m.getValuesTo();
						if (v != null) {
							valuesToMatch = v.getValue();
						} else {
							valuesToMatch = null;
						}
					}

					if (!Utils.emptyList(valuesToMatch)) {
						int i = 0;
						boolean noMatch = false;
						if (valuesToMatch.size() != value.size()) {
							noMatch = true;
						} else {
							for (String v : value) {
								if (!v.equals(valuesToMatch.get(i))) {
									noMatch = true;
									break;
								}
								i++;
							}
						}
						if (noMatch) {
							continue;
						} else {
							log.debug("Found mapping " + (from ? "From" : "To") + value + " " + (from ? "To" : "From")
									+ " " + (from ? m.getValuesTo().getValue() : m.getValuesFrom().getValue())
									+ (StringUtils.isNotBlank(m.getDescription()) ? " desc [" + description + "]"
											: ""));
							return (from ? m.getValuesTo().getValue() : m.getValuesFrom().getValue());
						}
					}
				}

				if (from) {
					if (map.getDefaultsTo() == null) {
						log.debug("No mapping found for [" + value + "] under [" + mapId + "]");
						return null;
					} else {
						log.debug("No mapping found for [" + value + "] under [" + mapId + "]");
						log.debug("Returning default " + map.getDefaultsTo().getValue());
						return map.getDefaultsTo().getValue();
					}
				} else {
					if (map.getDefaultsFrom() == null) {
						log.debug("No mapping found for [" + value + "] under [" + mapId + "]");
						return null;
					} else {
						log.debug("No mapping found for [" + value + "] under [" + mapId + "]");
						log.debug("Returning default " + map.getDefaultsFrom().getValue());
						return map.getDefaultsTo().getValue();
					}
				}

			}
		}
		log.debug("No match found");
		return null;
	}

	/**
	 * Initializes the mapping by parsing the xml file
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

		String xmlFileContent = FileUtils.readFile("Mapping File", this.inputFile);

		maps = GenericXmlMapMarshaller.getMapsObject(xmlFileContent);

		if (maps == null || Utils.emptyList(maps.getMap())) {
			log.warn("No mappings found");
			return;
		}
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

}
