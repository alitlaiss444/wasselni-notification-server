package com.wasselni.common.model.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audit {

	public enum Priority {
		LOW(1), MEDIUM(2), HIGH(3);

		private int value;

		private Priority(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public enum ParamIndex {
		ALL(-1);

		private int value;

		private ParamIndex(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public String auditAction() default "UNKNOWN";

	Priority priority() default Priority.MEDIUM;

	public boolean enabled() default true;

	public String[] mapping();

	public String screenName() default "UNKNOWN";
}
