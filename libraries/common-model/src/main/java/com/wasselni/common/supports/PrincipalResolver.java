package com.wasselni.common.supports;

import org.aspectj.lang.JoinPoint;

public interface PrincipalResolver {

	final String ANONYMOUS_USER = "audit:anonymous";

	final String UNKNOWN_USER = "audit:unknown";

	String resolveFrom(JoinPoint auditTarget, Object returnValue);

	String resolveFrom(JoinPoint auditTarget, Exception exception);

	String resolve();
}
