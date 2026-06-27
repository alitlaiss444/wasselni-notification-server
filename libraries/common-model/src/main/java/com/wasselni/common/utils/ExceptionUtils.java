package com.wasselni.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.domain.Result;
import com.wasselni.common.model.errors.exception.SystemException;

public class ExceptionUtils {

	private static final Log log = LogFactory.getLog(ExceptionUtils.class);

	private static List<String> skipStackError = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{

		}
	};

	/**
	 * Converts a call exception to internal system exception
	 * 
	 * @param e
	 * @throws SystemException
	 */
	public static void callExceptionToSystemException(Exception e) throws SystemException {

		if (e instanceof SystemException) {
			throw (SystemException) e;
		}

		if (e instanceof java.net.SocketException) {
			log.error("Failed calling webservice due to connection error", ExceptionUtils.limitStackTrace(e));
			Result result = Result.generate(SystemError.SE_COMMUNICATION_ERROR,
					"Failed calling webservice. Error Returned: " + e.getMessage());
			throw new SystemException(result);
		}

		if (e instanceof java.net.SocketTimeoutException) {
			log.error("Failed calling webservice, call timed-out");
			Result result = Result.generate(SystemError.SE_TIME_OUT, "Middleware service call");
			throw new SystemException(result);
		}

		log.error("Failed calling webservice", e);

		throw new SystemException(SystemError.SE_SYSTEM_ERROR, "Failed calling Webservice. Please check connectivity");
	}

	/**
	 * Handle exception: Generic function in each main interface function
	 * 
	 * @param e
	 * @return
	 */
	public static Result handleException(Exception e) {

		// Assume unknown exception and set the result as unknown error
		Result result = Result.generate(SystemError.UNKNOWN_ERROR);

		// If the exception is an instance of business exception, get the result
		// thrown by the exception
		if (e instanceof SystemException)
			result = ((SystemException) e).getResult();

		if (Utils.isInListNoDbg(skipStackError, result.getCode())) {
			log.error("Excpetion [" + e.getMessage() + "]");
		} else
			log.error("Error", limitStackTrace(e));

		return result;
	}

	/**
	 * Limits the stack trace output to only the specific package
	 * 
	 * @param error
	 */
	private static Throwable limitStackTrace(Throwable error) {

		StackTraceElement[] oldStack = error.getStackTrace();

		List<StackTraceElement> elements = new ArrayList<StackTraceElement>();

		int i = 0;

		for (StackTraceElement s : oldStack) {
			if (s.getClassName().startsWith("com.wasselni")) {
				i++;
				elements.add(s);
			}
		}

		StackTraceElement[] newStack = new StackTraceElement[1];

		if (elements.size() > 0)
			newStack = new StackTraceElement[elements.size()];

		if (elements.size() == 0) {
			newStack[0] = oldStack[0];
		} else {
			i = 0;
			for (StackTraceElement s : elements) {
				newStack[i] = s;
				i++;
			}
		}

		error.setStackTrace(newStack);

		return error;
	}
}
