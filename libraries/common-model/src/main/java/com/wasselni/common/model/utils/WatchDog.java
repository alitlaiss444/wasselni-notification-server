package com.wasselni.common.model.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import com.wasselni.common.model.errors.domain.Result;
import com.wasselni.common.utils.DebugUtils;

public class WatchDog {

	private static final Logger log = LogManager.getLogger(WatchDog.class);

	private Long startTime;
	private String fnName;
	private Long endTime;
	private Long timeConsumed;
	private Result result;
	private boolean mdcSet;
	private String operation;

	/**
	 * Creates an instance of the watchdog with required details and debugs entry to
	 * the function
	 * 
	 * @param fnName function name to show in the logs
	 */
	public WatchDog(String fnName) {
		super();
		this.fnName = fnName;
		startTime = System.currentTimeMillis();
		mdcSet = false;
		if (StringUtils.isNotBlank(fnName))
			DebugUtils.debugEntryService(fnName);
	}

	/**
	 * Creates an instance of the watchdog with required details and debugs entry to
	 * the function. If the redirectToFileName is specified, MDC will set the
	 * thread_name variable to the value specified. If correctly configured in the
	 * logback.xml, the debug file name will be updated based on the way thread_name
	 * is used in it
	 * 
	 * @param fnName             function name to show in the logs
	 * @param redirectToFileName value to use for the {@link MDC} thread_name
	 */
	public WatchDog(String fnName, String redirectToFileName) {
		super();
		if (StringUtils.isNotBlank(redirectToFileName)) {
			DebugUtils.emphasize("Redirecting to log file [" + redirectToFileName + "]");
			MDC.put("thread_name", redirectToFileName);
			mdcSet = true;
		}

		this.fnName = fnName;
		startTime = System.currentTimeMillis();
		DebugUtils.debugEntryService(fnName);
	}

	/**
	 * Closes the watchdog and displays the exit from the function/section with the
	 * processing time taken by it. If the {@link MDC} is set, the value is cleared
	 */
	public long stop() {
		endTime = System.currentTimeMillis();
		timeConsumed = endTime - startTime;
		if (StringUtils.isNotBlank(fnName))
			DebugUtils.debugExitService(fnName, startTime);
		if (mdcSet) {
			MDC.clear();
		}
		return timeConsumed;

	}

	/**
	 * Closes the watchdog and displays the exit from the function/section with the
	 * processing time taken by it. If the {@link MDC} is set, the value is cleared.
	 * The result is also printed showing if an error occurred or not
	 * 
	 * @param result Result to print in the logs
	 */
	public void stop(Result result) {
		endTime = System.currentTimeMillis();
		timeConsumed = endTime - startTime;
		this.result = result;
		DebugUtils.debugExitService(fnName, result, startTime);
		MDC.clear();
		result.setProcessingTime(timeConsumed);
	}

	public void end() {
		log.debug(this.operation + " took: " + +((((double) (System.currentTimeMillis())) - startTime) / 1000.00)
				+ " seconds.");
	}

	public Long getTimeConsumed() {
		return timeConsumed;
	}

	public Result getResult() {
		return result;
	}

}
