
package com.wasselni.common.model.localization;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalizeSerializer extends StdSerializer<Object> {

	private static final long serialVersionUID = -2391442805192997903L;
	private @Autowired MessageSource messageSource;

	public LocalizeSerializer() {
		super(Object.class);
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

		String returnValue = getLocalizeValue(value);

		if (returnValue != null)
			gen.writeString(returnValue);
		else
			gen.writeNull();

	}

	private String getLocalizeValue(Object value) {
		try {

			String objValue = (String) value;

			if (StringUtils.isBlank(objValue))
				return objValue;

			if (!objValue.startsWith("{") && !objValue.endsWith("}"))
				return objValue;

			// Remove first and last characters

			objValue = objValue.substring(1, objValue.length() - 1);

			return messageSource.getMessage(objValue, null, LocaleContextHolder.getLocale());

		} catch (Exception e) {
			return (String) value;
		}
	}
}