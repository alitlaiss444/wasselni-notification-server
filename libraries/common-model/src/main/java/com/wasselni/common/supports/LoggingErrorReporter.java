package com.wasselni.common.supports;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LoggingErrorReporter implements ErrorReporter {

	private static final Logger log = LogManager.getLogger(ErrorReporter.class);

	public void reportError(final String principal, final String description) {
		logErrorRecord(principal, description);
	}

	public void reportError(final String principal, final Throwable throwable) {
		final StackTraceElement[] stackTraceElements = throwable.getStackTrace();
		final StringBuilder builder = new StringBuilder(512);
		builder.append("\n\t").append(throwable.toString());

		for (final StackTraceElement element : stackTraceElements) {
			builder.append("\n\tat ").append(element.toString());
		}

		logErrorRecord(principal, builder.toString());

	}

	private void logErrorRecord(final String principal, final String details) {
		final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		final StringBuilder builder = new StringBuilder(512);
		builder.append("\n\n===ERROR LOG record BEGIN==========================================")
				.append("\nPRINCIPAL: ").append(principal).append("\nWHEN: ").append(dateTimeFormat.format(new Date()))
				.append("\nDESCRIPTION: ").append(details)
				.append("\n===ERROR LOG record END============================================").append("\n\n");
		log.error(builder.toString());
	}

}
