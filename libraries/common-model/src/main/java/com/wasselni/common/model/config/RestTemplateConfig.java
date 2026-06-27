
package com.wasselni.common.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RestTemplateConfig {

	private @Value("${portal.api.connect.timeout:10000}") Integer connectTimeout;
	private @Value("${portal.api.request.timeout:10000}") Integer requestTimeOut;
	private @Value("${portal.api.max.routes:50}") Integer maxRoutes;

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getRequestTimeOut() {
		return requestTimeOut;
	}

	public void setRequestTimeOut(Integer requestTimeOut) {
		this.requestTimeOut = requestTimeOut;
	}

	public Integer getMaxRoutes() {
		return maxRoutes;
	}

	public void setMaxRoutes(Integer maxRoutes) {
		this.maxRoutes = maxRoutes;
	}

}
