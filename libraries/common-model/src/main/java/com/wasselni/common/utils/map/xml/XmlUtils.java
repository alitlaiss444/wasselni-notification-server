package com.wasselni.common.utils.map.xml;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

public class XmlUtils {

	private static final Log log = LogFactory.getLog(XmlUtils.class);

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
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(new String(xml.getBytes(), "UTF-8").getBytes()));
			return doc.getDocumentElement().getChildNodes();

		} catch (Exception e) {
			log.error("Failed getting XML node list", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed getting node list for XML String");
		}

	}

	/**
	 * Gets the node list with xml validation
	 * 
	 * @param xml
	 * @param validateEntity
	 * @return
	 * @throws SystemException
	 */
	public NodeList getNodeList(String xml, boolean validateEntity) throws SystemException {
		try {

			if (StringUtils.isBlank(xml))
				return null;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			if (!validateEntity) {
				dbf.setValidating(false);
				dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			}

			DocumentBuilder dBuilder = dbf.newDocumentBuilder();

			Document doc = dBuilder.parse(new ByteArrayInputStream(new String(xml.getBytes(), "UTF-8").getBytes()));
			return doc.getDocumentElement().getChildNodes();

		} catch (Exception e) {
			log.error("Failed getting XML node list", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed getting node list for XML String");
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

	public void parseXml(String xml, boolean validateEntity) {
		try {

			NodeList nl = getNodeList(xml, validateEntity);

			for (int k = 0; k < nl.getLength(); k++) {
				printTags((Node) nl.item(k));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printTags(Node nodes) {

		if (nodes.hasChildNodes() || nodes.getNodeType() != 3) {
			System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
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

		if (nodeName.contains(":")) {
			nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
		}
		return nodeName;
	}

	/**
	 * Converts an XML string to an XML document
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Converts an XML document to an XML char
	 * 
	 * @param doc
	 * @return
	 */
	public static String convertDocumentToString(Document doc) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			// below code to remove XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
			// "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets a tag value from an XML string and returns the default specified in case
	 * not found
	 * 
	 * @param xml
	 * @param tag
	 * @return
	 */
	public static String getTagValue(Document doc, String tag, String defaultValue) {

		Node n = doc.getFirstChild();
		String returnVal = getTagValue(n, tag, defaultValue);
		return returnVal == null ? defaultValue : returnVal;
	}

	public static String getTagValue(Node n, String tag, String defaultValue) {

		if ((StringUtils.isNotBlank(n.getNodeName()) && n.getNodeName().toUpperCase().equals(tag.toUpperCase()))
				|| (StringUtils.isNotBlank(n.getLocalName())
						&& n.getLocalName().toUpperCase().equals(tag.toUpperCase()))) {

			NodeList nl2 = n.getChildNodes();

			Node an = nl2.item(0);

			if (an != null && an.getNodeType() == Node.TEXT_NODE)
				return an.getTextContent();
		}

		NodeList nl = n.getChildNodes();
		Node an = null;

		for (int i = 0; i < nl.getLength(); ++i) {
			an = nl.item(i);
			if (an.getNodeType() == Node.ELEMENT_NODE) {
				String val = getTagValue(an, tag, defaultValue);
				if (val != null)
					return val;
			}
		}

		return null;
	}

	/**
	 * Gets a node from a document using the tag specified and ignoring the
	 * namespaces. First occurrence found will be returned
	 * 
	 * @param doc document to get the node from
	 * @param tag tag to get the node for
	 * @return {@link Node} for the specified tag in case found, null otherwise
	 */
	public static Node getNodeByTagName(Document doc, String tag) {

		Node n = doc.getFirstChild();
		NodeList nl = n.getChildNodes();
		Node an = null;

		for (int i = 0; i < nl.getLength(); i++) {
			an = nl.item(i);
			if (StringUtils.isNotBlank(an.getLocalName()) && an.getLocalName().equals(tag))
				return an;
		}
		return null;
	}
}
