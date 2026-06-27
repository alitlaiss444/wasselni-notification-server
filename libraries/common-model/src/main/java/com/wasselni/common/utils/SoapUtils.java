package com.wasselni.common.utils;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SoapUtils {

	enum KeyIdentifierType {
		NOT_SPECIFIED, THUMBPRINT, X509_CERT_DIRECT, BST_DIRECT_REFERENCE, RSA_KEY_VALUE, ISSUER_SERIAL;

		static KeyIdentifierType fromString(String s) {
			for (KeyIdentifierType t : KeyIdentifierType.values()) {
				if (t.name().equals(s))
					return t;
			}
			return KeyIdentifierType.NOT_SPECIFIED;
		}
	}

	public static final String X509_PKIPathv1 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509PKIPathv1";

	public static final String X509_V3_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";

	public static final String THUMBPRINT_SHA1 = "http://docs.oasis-open.org/wss/oasis-wss-soap-message-security1.1#ThumbprintSHA1";

	public static final String BASE64_BINARY = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";

	public static final String SIGNING_METHOD_RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
	public static final String SIGNING_METHOD_RSA_SHA1 = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";

	private static final Log log = LogFactory.getLog(SoapUtils.class);

	/**
	 * Adds the soap related details (envelope) the body
	 * 
	 * @param body
	 * @param header
	 * @return
	 */
	public static String addSoapenvEnvelope(String body, String header) {

		if (StringUtils.isBlank(header))
			header = "";
		String output = "" + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Header>" + header + "</soapenv:Header>" + "<soapenv:Body>" + body + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return output;
	}


	public static String getNamespaces(String uri, HashMap<String, String> namespaces) {
		String prefix = namespaces.get(uri);

		if (StringUtils.isNotBlank(prefix))
			return prefix;

		prefix = "ns" + namespaces.size();
		namespaces.put(uri, prefix);
		return prefix;
	}

	public static Element getNextSiblingElement(Node node) {
		Node sibling = node.getNextSibling();
		while ((sibling != null) && (sibling.getNodeType() != Node.ELEMENT_NODE)) {
			sibling = sibling.getNextSibling();
		}
		return (Element) sibling;
	}

	/**
	 * Strips the soap envelope of a response string
	 * 
	 * @param response
	 * @return
	 */
	public static String stripSoapEnvelope(String response) {
		String output = null;

		int firstIndexOfBody = response.indexOf(":Body>");
		int lastIndexOfBody = response.lastIndexOf(":Body>");

		if (firstIndexOfBody > 0 && lastIndexOfBody > firstIndexOfBody) {
			output = response.substring(firstIndexOfBody + (":Body>").length(), lastIndexOfBody - 2);
			output = output.substring(0, output.lastIndexOf("</"));
		} else {
			log.error("Could not find body in the response message");
		}

		return output;
	}

	/**
	 * Adds the soap related details (envelope) the body
	 * 
	 * @param body
	 * @param header
	 * @return
	 */
	public static String addSoapEnvelope(String body, String header) {
		String output = "" + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Header>" + header + "</soapenv:Header>" + "<soapenv:Body>" + body + "</soapenv:Body>"
				+ "</soapenv:Envelope>";

		return output;
	}

}
