package com.wasselni.common.supports;

/**
 *
 */
public final class AuditHolder {

	private static final ThreadLocal<Object> data = new ThreadLocal<Object>();

	public static void setObject(Object object) {
		data.set(object);
	}

	public static Object getObject() {
		return data.get();
	}

	public static void reset() {
		data.set(null);
	}
}
