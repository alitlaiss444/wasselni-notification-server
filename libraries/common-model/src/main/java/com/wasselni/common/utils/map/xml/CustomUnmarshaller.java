package com.wasselni.common.utils.map.xml;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.domain.Result;
import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.common.utils.ExceptionUtils;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlValue;

public class CustomUnmarshaller {

	private static final Log log = LogFactory.getLog(CustomUnmarshaller.class);

	private XmlUtils xmlUtils = new XmlUtils();

	/**
	 * Interface use for casting the string to the appropriate object
	 */
	public interface CastFunction {
		public Object cast(String value);
	}

	private static CastFunction IntegerCastFunction = new CastFunction() {
		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return Integer.valueOf(value);
		}
	};

	private static CastFunction StringCastFunction = new CastFunction() {
		@Override
		public Object cast(String value) {
			return value;
		}
	};

	private static CastFunction BigIntegerCastFunction = new CastFunction() {
		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return new BigInteger(value);
		}
	};

	private static CastFunction BigDecimalCastFunction = new CastFunction() {

		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return new BigDecimal(value);
		}
	};

	private static CastFunction LongCastFunction = new CastFunction() {

		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return Long.valueOf(value);
		}

	};

	private static CastFunction ShortCastFunction = new CastFunction() {
		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return Short.valueOf(value);
		}
	};

	private static CastFunction FloatCastFunction = new CastFunction() {
		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return Float.valueOf(value);
		}
	};

	private static CastFunction BooleanCastFunction = new CastFunction() {

		@Override
		public Object cast(String value) {
			if (StringUtils.isBlank(value))
				return null;
			else
				return value.equalsIgnoreCase("true");
		}

	};

	private static Map<String, CastFunction> simpleTypes = new HashMap<String, CastFunction>() {
		private static final long serialVersionUID = 1L;

		{
			put(Integer.class.getName(), IntegerCastFunction);
			put(int.class.getName(), IntegerCastFunction);
			put(String.class.getName(), StringCastFunction);
			put(BigInteger.class.getName(), BigIntegerCastFunction);
			put(BigDecimal.class.getName(), BigDecimalCastFunction);
			put(Long.class.getName(), LongCastFunction);
			put(long.class.getName(), LongCastFunction);
			put(Short.class.getName(), ShortCastFunction);
			put(short.class.getName(), ShortCastFunction);
			put(Float.class.getName(), FloatCastFunction);
			put(float.class.getName(), FloatCastFunction);
			put(Boolean.class.getName(), BooleanCastFunction);
			put(boolean.class.getName(), BooleanCastFunction);
		}
	};

	public <T> T unmarshall(String xml, Class<T> clazz) throws SystemException {

		if (StringUtils.isBlank(xml))
			throw new SystemException(SystemError.SE_EXTERNAL_SYSTEM_ERROR, "Empty response from Middleware");

		try {

			log.trace("Unmarshalling [" + xml + "]");
			log.trace("Class name requried [" + clazz.getName() + "]");

			if (log.isTraceEnabled())
				xmlUtils.parseXml(xml, false);

			T out = clazz.newInstance();

			NodeList nodeList = xmlUtils.getNodeList(xml, false);

			Node rootNode = getRootNodeForObject(out.getClass().getSimpleName(), nodeList);

			if (rootNode == null) {
				throw new SystemException(SystemError.SE_SYSTEM_ERROR,
						"Invalid Response Received from MW: Root node [" + clazz.getSimpleName() + "] not found");
			}

			fillObjectFromNode(out, rootNode);

			return out;
		} catch (Exception e) {
			log.error("Failed unmarshalling string [" + xml + "] to object type [" + clazz.getName() + "]", e);
			Result result = ExceptionUtils.handleException(e);
			throw new SystemException(result);
		}

	}

	/**
	 * Gets the node matching the required object name
	 * 
	 * @param requiredName
	 * @param nodeList
	 * @return
	 */
	private Node getRootNodeForObject(String requiredName, NodeList nodeList) {

		if (nodeList == null)
			return null;

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node childNode = (Node) nodeList.item(i);

			String name = xmlUtils.stripNamespacePrefix(childNode.getNodeName());

			log.trace("Checking name [" + name + "]");

			if (childNode.getNodeType() != Node.TEXT_NODE) {
				if (name.equalsIgnoreCase(requiredName)) {
					log.trace("Found root node");
					return childNode;
				}
				if (childNode.hasChildNodes()) {
					childNode = getRootNodeForObject(requiredName, childNode.getChildNodes());
					if (childNode != null)
						return childNode;
				}
			}
		}

		log.trace("No nodes found matching required name under node list");
		return null;
	}

	/**
	 * Fills an object from the provided node
	 * 
	 * @param o
	 * @param node
	 * @throws IllegalAccessException
	 * @throws DOMException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws SystemException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings({ "unchecked" })
	private void fillObjectFromNode(Object o, Node node)
			throws IllegalArgumentException, DOMException, IllegalAccessException, InstantiationException,
			SystemException, ClassNotFoundException, NoSuchFieldException, SecurityException {

		if (node == null)
			return;

		List<Field> fieldsList = new ArrayList<Field>();
		Class<?> clazz = o.getClass();
		while (!Object.class.getName().equals(clazz.getName())) {
			fieldsList.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}

		Field[] fields = new Field[fieldsList.size()];

		fieldsList.toArray(fields);

		for (Field f : fields) {

			f.setAccessible(true);

			/*
			 * if (!f.isAnnotationPresent(XmlElement.class) && !f
			 * .isAnnotationPresent(XmlAttribute.class)) {
			 * 
			 * if (debug) log.debug("Annotation not found for field [" + f.getName() + "]");
			 * continue;
			 * 
			 * }
			 */

			String elementName = f.getName();
			if (f.isAnnotationPresent(XmlElement.class)) {
				String annotationElementName = f.getAnnotation(XmlElement.class).name().trim();
				if (StringUtils.isNotBlank(annotationElementName) && !"##default".equals(annotationElementName))
					elementName = annotationElementName;
			}

			/*
			 * if (f.isAnnotationPresent(XmlElement.class)) elementName =
			 * f.getAnnotation(XmlElement.class).name(); else elementName =
			 * f.getAnnotation(XmlAttribute.class).name();
			 */

			log.trace("Node with childs, checking individual ones");
			NodeList nodeList = node.getChildNodes();

			String listNodeName = null;

			if (node.getAttributes() != null && node.getAttributes().getLength() > 0) {
				for (int i = 0; i < node.getAttributes().getLength(); i++) {
					if (node.getAttributes().item(i).getNodeName().toLowerCase().equalsIgnoreCase(elementName)) {
						CastFunction function = simpleTypes.get(f.getType().getName());
						if (function != null)
							f.set(o, function.cast(node.getAttributes().item(i).getNodeValue()));
						else
							f.set(o, node.getAttributes().item(i).getNodeValue());
					}
				}
			}

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node childNode = (Node) nodeList.item(i);

				String name = xmlUtils.stripNamespacePrefix(childNode.getNodeName());

				if (childNode.getNodeType() == Node.TEXT_NODE && f.isAnnotationPresent(XmlValue.class)) {
					f.set(o, childNode.getNodeValue());
				} else if (childNode.getNodeType() != Node.TEXT_NODE) {

					log.trace("Checking node [" + name + "] for field [" + elementName + "]");

					if (name.equalsIgnoreCase(elementName)) {

						log.trace("Found match, creating new instance " + "of child object and filling it");
						log.trace("Field type [" + f.getType().getName() + "]");

						if (simpleTypes.get(f.getType().getName()) != null) {

							if (!(childNode instanceof Element)) {
								log.trace("Simple type not instance of element");
								break;
							}

							Element element = (Element) childNode;

							if (StringUtils.isBlank(element.getTextContent())) {

								log.trace("Empty element, not setting it");
							} else {

								log.trace("Setting field [" + f.getName() + "] to value ["
										+ simpleTypes.get(f.getType().getName()).cast(element.getTextContent()) + "]");

								f.set(o, simpleTypes.get(f.getType().getName()).cast(element.getTextContent()));

							}
							break;
						}
						// if it's an enum get the from value
						if (f.getType() instanceof Class && ((Class<?>) f.getType()).isEnum()) {
							Element element = (Element) childNode;

							if (StringUtils.isBlank(element.getTextContent())) {

								log.trace("Empty element, not setting it");
							} else {

								Object[] enums = f.getType().getEnumConstants();

								Field ff = enums[0].getClass().getDeclaredField("value");
								ff.setAccessible(true);
								for (Object e : enums) {
									String value = ff.get(e).toString();
									if (value.equals(element.getTextContent())) {
										log.trace("Setting field [" + f.getName() + "] to value ["
												+ element.getTextContent() + "]");

										f.set(o, e);

										break;
									}
								}
							}
							break;

						}
						// Do not break in list until the last element of the
						// list is processed
						else if (f.getType().getName().equalsIgnoreCase(java.util.List.class.getName())) {

							if (StringUtils.isNotBlank(listNodeName)) {

								if (!childNode.getNodeName().equalsIgnoreCase(listNodeName)) {
									log.trace("FATAL Reached end of list");
									break;
								}

							} else {
								listNodeName = childNode.getNodeName();
							}

							Type type = f.getGenericType();

							ParameterizedType pt = (ParameterizedType) type;

							if (f.get(o) == null) {

								log.trace("List for field [" + f.getName() + "] null, creating new instance");
								f.set(o, new ArrayList<Object>());
							}

							String typeString = getClassName(pt.getActualTypeArguments()[0]);

							if (simpleTypes.get(typeString) != null) {
								if (!(childNode instanceof Element)) {

									log.trace("Simple type not instance of element");
									break;
								}

								Element element = (Element) childNode;

								if (StringUtils.isBlank(element.getTextContent())) {
									log.trace("Empty element not setting it");
								} else {
									((List<Object>) f.get(o))
											.add(simpleTypes.get(typeString).cast(element.getTextContent()));
								}
							} else {

								Object childObject = Class.forName(typeString).newInstance();
								fillObjectFromNode(childObject, childNode);
								((List<Object>) f.get(o)).add(childObject);

								log.trace("Number of elements for field list [" + f.getName() + "]["
										+ ((List<Object>) f.get(o)).size() + "]");
							}

						} else if (f.getType().getName()
								.equalsIgnoreCase(javax.xml.datatype.XMLGregorianCalendar.class.getName())) {

							if (!(childNode instanceof Element)) {
								log.trace("Simple type not instance of element");
								break;
							}
							Element element = (Element) childNode;
							if (StringUtils.isBlank(element.getTextContent())) {
								log.trace("Empty element not setting it");
							} else {

								try {
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
									Date date = df.parse(element.getTextContent());
									GregorianCalendar cal = new GregorianCalendar();
									cal.setTime(date);
									javax.xml.datatype.XMLGregorianCalendar xmlDate;
									cal.getTimeZone();
									xmlDate = DatatypeFactory.newInstance()
											.newXMLGregorianCalendar(cal.get(Calendar.YEAR),
													cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
													cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
													cal.get(Calendar.SECOND), DatatypeConstants.FIELD_UNDEFINED,
													TimeZone.LONG)
											.normalize();
									f.set(o, xmlDate);
								} catch (DatatypeConfigurationException e) {
									log.warn("Failed parsing date [" + element.getTextContent() + "]", e);
								} catch (ParseException e) {
									log.warn("Failed parsing date [" + element.getTextContent() + "]", e);
								}

							}

						} else if (f.getType().getName().equalsIgnoreCase(JAXBElement.class.getName())) {

							log.trace("Detected jaxbElement");

							Type type = f.getGenericType();

							ParameterizedType pt = (ParameterizedType) type;

							String typeString = getClassName(pt.getActualTypeArguments()[0]);

							if (simpleTypes.get(typeString) != null) {
								if (!(childNode instanceof Element)) {
									log.trace("Simple type not instance of element");
									break;
								}

								Element element = (Element) childNode;

								if (StringUtils.isBlank(element.getTextContent())) {
									log.trace("Empty element not setting it");
								} else {

									JAXBElement<?> jaxbElement = new JAXBElement<Object>(
											new QName("", childNode.getNodeName()), Object.class,
											simpleTypes.get(typeString).cast(element.getTextContent()));
									f.set(o, jaxbElement);
								}
							} else {

								Object childObject = Class.forName(typeString).newInstance();
								fillObjectFromNode(childObject, childNode);

								JAXBElement<?> jaxbElement = new JAXBElement<Object>(
										new QName("", childNode.getNodeName()), Object.class, childObject);

								f.set(o, jaxbElement);
							}

							break;

						} else {
							try {
								Object childObject = f.getType().newInstance();
								fillObjectFromNode(childObject, childNode);
								f.set(o, childObject);
								break;
							} catch (Exception e) {
								e.printStackTrace();
								log.error("Failed initializing [" + f.getType() + "] of element [" + f.getName() + "]",
										e);
							}
						}
					}
				}
			}
		}
	}

	static String NAME_PREFIX = "class ";

	private String getClassName(Type type) {
		String fullName = type.toString();
		if (fullName.startsWith(NAME_PREFIX))
			return fullName.substring(NAME_PREFIX.length());
		return fullName;
	}
}
