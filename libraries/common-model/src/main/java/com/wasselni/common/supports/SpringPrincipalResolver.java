package com.wasselni.common.supports;

import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SpringPrincipalResolver implements PrincipalResolver {

	public String resolveFrom(final JoinPoint auditableTarget, final Object retval) {
		return getFromSecurityContext();
	}

	public String resolveFrom(final JoinPoint auditableTarget, final Exception exception) {
		return getFromSecurityContext();
	}

	public String resolve() {
		return getFromSecurityContext();
	}

	private String getFromSecurityContext() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();

		if (securityContext == null) {
			return null;
		} else if (securityContext.getAuthentication() == null) {
			return null;
		}

		return securityContext.getAuthentication().getName();
	}

}
