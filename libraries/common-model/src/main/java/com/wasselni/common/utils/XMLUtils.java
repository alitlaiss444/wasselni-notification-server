package com.wasselni.common.utils;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

public class XMLUtils {

	private static final Log log = LogFactory.getLog(XMLUtils.class);

	/**
	 * Gets the node list for the specified XML string
	 * 
	 * @param xml
	 * @return
	 * @throws SystemException
	 */
	public NodeList getNodeList(String xml) throws SystemException {
		try {

			if (StringUtils.isBlank(xml))
				return null;
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(
					new String(xml.getBytes(), "UTF-8").getBytes()));
			return doc.getDocumentElement().getChildNodes();

		} catch (Exception e) {
			log.error("Failed getting XML node list", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR,
					"Failed getting node list for XML String");
		}

	}

	public void parseXml(String xml) {
		try {

			NodeList nl = getNodeList(xml);

			for (int k = 0; k < nl.getLength(); k++) {
				printTags((Node) nl.item(k));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printTags(Node nodes) {

		if (nodes.hasChildNodes() || nodes.getNodeType() != 3) {
			System.out.println(
					nodes.getNodeName() + " : " + nodes.getTextContent());
			NodeList nl = nodes.getChildNodes();
			for (int j = 0; j < nl.getLength(); j++)
				printTags(nl.item(j));
		}
	}

	/**
	 * Removes the namespace prefix from a node name
	 * 
	 * @param nodeName
	 * @return
	 */
	public String stripNamespacePrefix(String nodeName) {
		if (StringUtils.isBlank(nodeName))
			return nodeName;
		
		if (nodeName.contains(":")){
			nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
		}
		return nodeName;
	}
}
