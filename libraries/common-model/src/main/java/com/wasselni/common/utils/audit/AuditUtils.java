/**
 * 
 */
package com.wasselni.common.utils.audit;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.wasselni.common.model.audit.AuditConstants;
import com.wasselni.common.model.audit.ClassAttribute;
import com.wasselni.common.model.wrapper.response.Response;

public class AuditUtils {

	public static int evaluateResult(Object returnObject) {

		if (returnObject == null) {
			return AuditConstants.AUDIT_ACTION_FAILED;
		}

		if (returnObject instanceof Response) {
			return ((Response<?>) returnObject).isSuccess() ? AuditConstants.AUDIT_ACTION_SUCCESS
					: AuditConstants.AUDIT_ACTION_FAILED;
		}

		return AuditConstants.AUDIT_ACTION_SUCCESS;
	}

	public static boolean isArrayUsable(Object[] objects) {
		return (objects != null && objects.length > 0);
	}

	public static ArrayList<ClassAttribute> dissect(Object object) {

		if (object == null) {
			return null;
		}

		return parse(object);

	}

	public static ArrayList<ClassAttribute> parse(Object obj) {
		if (obj == null) {
			return new ArrayList<ClassAttribute>();
		}

		Class<?> c = obj.getClass();
		Field[] fields = c.getDeclaredFields();
		ArrayList<ClassAttribute> temp = new ArrayList<ClassAttribute>();

		for (Field field : fields) {
			try {
				field.setAccessible(true);
				temp.add(new ClassAttribute(toReadable(field.getName().toString()), field.get(obj)));
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}

		return temp;
	}

	public static String toReadable(String variable) {
		return variable.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ").toUpperCase();
	}

	public static Date getNow() {
		return new Date();
	}

	public static String getTime() {
		return new SimpleDateFormat("HH:mm").format(new Date());
	}

}
