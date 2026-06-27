package com.wasselni.common.supports;

public interface ErrorReporter {

	void reportError(String principal, String errorDescription);

	void reportError(String principal, Throwable throwable);

}
