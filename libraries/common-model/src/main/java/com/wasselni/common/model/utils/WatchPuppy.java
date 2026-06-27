package com.wasselni.common.model.utils;

import org.apache.commons.lang3.StringUtils;

import com.wasselni.common.utils.DebugUtils;

/**
 * 
 * Utility domain object to display logging entries and the time consumed mainly
 * used for small functions for which the timing is required
 *
 */
public class WatchPuppy {

	private Long startTime;
	private String fnName;
	private Long endTime;
	private Long timeConsumed;

	/**
	 * Creates an instance of the watchpuppy with required details and debugs entry
	 * to the function
	 * 
	 * @param fnName function name to display in the logs
	 */
	public WatchPuppy(String fnName) {
		super();
		this.fnName = fnName;
		startTime = System.currentTimeMillis();
		if (StringUtils.isNotBlank(fnName))
			DebugUtils.debugEntryFunction(fnName);
	}

	/**
	 * Closes the watchpuppy and displays the exit from the function/section with
	 * the processing time taken by it
	 */
	public long stop() {
		endTime = System.currentTimeMillis();
		timeConsumed = endTime - startTime;
		if (StringUtils.isNotBlank(fnName))
			DebugUtils.debugExitFunction(fnName, startTime);
		return timeConsumed;
	}

	public Long getTimeConsumed() {
		return timeConsumed;
	}

}
