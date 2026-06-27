package com.wasselni.common.utils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.exception.SystemException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class JaxBUtils {

	private static final Log log = LogFactory.getLog(JaxBUtils.class);

	private JAXBContext jaxbContext = null;
	private Class<?> ca;
	private boolean beautify;

	public JaxBUtils(Class<?> clazz) throws JAXBException {
		this(clazz, false);
	}

	public JaxBUtils(Class<?> clazz, boolean beautify) throws JAXBException {
		super();
		this.jaxbContext = JAXBContext.newInstance(clazz);
		this.ca = clazz;
		this.beautify = beautify;

	}

	/**
	 * 
	 * @param properties
	 * @param implicitCdata
	 * @return
	 * @throws SystemException
	 * @throws JAXBException
	 */
	public Marshaller getMarshaller(Map<String, Object> properties) throws SystemException {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

			if (!Utils.emptyMap(properties)) {
				for (Map.Entry<String, Object> entry : properties.entrySet())
					marshaller.setProperty(entry.getKey(), entry.getValue());
			}
			return marshaller;
		} catch (JAXBException e) {
			log.error("Error creating marshaller", e);
			throw new SystemException(SystemError.UNKNOWN_ERROR);
		}
	}

	/**
	 * Converts the provided object to an xml string
	 * 
	 * @param o
	 * @return
	 * @throws SystemException
	 */
	public String marshall(Object o, Map<String, Object> properties) throws SystemException {

		if (o == null)
			return "";

		String response = null;
		try {

			Marshaller marshaller = getMarshaller(properties);
			StringWriter w = new StringWriter();
			marshaller.marshal(o, w);
			response = w.toString();
		} catch (Exception e) {
			DebugUtils.dumpObject(o);
			log.error("Failed marshalling object of type [" + ca + "]", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed converting object to xml");
		} catch (Throwable e) {
			log.error("Failed marshalling object of type [" + ca + "]", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed converting object to xml");
		}

		return response;
	}

	/**
	 * Converts the provided object to a writer
	 * 
	 * @param o
	 * @return
	 * @throws SystemException
	 */
	public void marshall(Object o, Writer out) throws SystemException {

		marshall(o, getMarshaller(null), out);

	}

	/**
	 * Converts the provided object to a writer
	 * 
	 * @param o
	 * @return
	 * @throws SystemException
	 */
	public void marshall(Object o, Map<String, Object> properties, Writer out) throws SystemException {

		marshall(o, getMarshaller(properties), out);

	}

	/**
	 * Converts the provided object to a writer
	 * 
	 * @param o
	 * @return
	 * @throws SystemException
	 */
	public void marshall(Object o, Marshaller marshaller, Writer out) throws SystemException {

		if (o == null || out == null || marshaller == null)
			return;

		try {
			marshaller.marshal(o, out);
		} catch (Exception e) {
			DebugUtils.dumpObject(o);
			log.error("Failed marshalling object of type [" + ca + "]", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed converting object to xml");
		}

	}

	/**
	 * Converts the provided object to an xml string
	 * 
	 * @param o
	 * @return
	 * @throws SystemException
	 */
	public String marshall(Object o) throws SystemException {

		Map<String, Object> obj = null;

		return marshall(o, obj);
	}

	public QName get(Class<?> clazz) throws SystemException {
		return ((JAXBContextImpl) jaxbContext).getRuntimeTypeInfoSet().getClassInfo(clazz).getTypeName();
	}
	

	/**
	 * Converts the provided object to an xml string
	 * 
	 * @param o
	 * @param clazz
	 * @return
	 * @throws SystemException
	 */
	public String marshall(Object o, Class<?> clazz) throws SystemException {

		if (o == null)
			return "";

		String response = null;
		try {

			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			StringWriter w = new StringWriter();
			marshaller.marshal(o, w);
			response = w.toString();
		} catch (Exception e) {
			DebugUtils.dumpObject(o);
			log.error("Failed marshalling object of type [" + clazz + "]", e);
			throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed converting object to xml");
		}

		return response;
	}
}
