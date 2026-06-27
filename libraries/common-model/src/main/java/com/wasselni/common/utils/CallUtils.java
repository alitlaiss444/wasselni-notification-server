package com.wasselni.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.wasselni.common.model.errors.constants.ErrorType;
import com.wasselni.common.model.errors.constants.SystemError;
import com.wasselni.common.model.errors.domain.Result;
import com.wasselni.common.model.errors.exception.SystemException;
import com.wasselni.common.model.utils.WatchPuppy;

@SuppressWarnings("deprecation")
public class CallUtils {

	private static final Log log = LogFactory.getLog(CallUtils.class);

	private static ExecutorService executor;

	private static SSLContext noopContext;
	private static TrustManager[] noopTrustManager;
	private static CustomHostnameVerifier customHostnameVerififier;

	static {
		noopContext = insecureContext();
		customHostnameVerififier = new CustomHostnameVerifier();
	}

	private static class CustomHostnameVerifier implements org.apache.http.conn.ssl.X509HostnameVerifier {

		@Override
		public boolean verify(String host, SSLSession session) {
//			HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//			return hv.verify(host, session);
			return true;
		}

		@Override
		public void verify(String host, SSLSocket ssl) throws IOException {
		}

		@Override
		public void verify(String host, X509Certificate cert) throws SSLException {

		}

		@Override
		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

		}
	}

	private static SSLContext insecureContext() {
		noopTrustManager = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}

			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("ssl");
			sc.init(null, noopTrustManager, null);
			return sc;
		} catch (KeyManagementException | NoSuchAlgorithmException ex) {
		}
		return null;
	}

	/**
	 * Sends a request to the URL specified and returns the response using the UTF-8
	 * encoding
	 * 
	 * @param URL               URL to sent the request to
	 * @param request           request to send
	 * @param timeout           time out for the request
	 * @param connectionTimeout connection time-out for the request
	 * @param soTimeout         socket time out
	 * @return Response string
	 * @throws SystemException
	 */
	public static Result getResponseByXML(String URL, String action, String request, int timeout, int connectionTimeout,
			int soTimeout) throws SystemException {

		return getResponseByXML(URL, action, request, null, timeout, connectionTimeout, soTimeout, null, false, null);
	}

	/**
	 * Sends a request to the URL specified and returns the response. Supports
	 * specifying the character set to be used
	 * 
	 * @param URL               URL to sent the request to
	 * @param request           request to send
	 * @param timeout           time out for the request
	 * @param connectionTimeout connection time-out for the request
	 * @param soTimeout         socket time out
	 * @param characterSet      character set to be used
	 * @param trustAllCerts
	 * @return Response string
	 * @throws SystemException
	 */
	public static Result getResponseByXML(String URL, String soapAction, String request, Map<String, String> headers,
			int timeout, int connectionTimeout, int soTimeout, String characterSet, boolean trustAllCerts,
			String protocol) throws SystemException {

		WatchPuppy wp = new WatchPuppy("Service Call");

		Result result = Result.generate(SystemError.SUCCESS);

		Charset charset = null;
		if (StringUtils.isBlank(characterSet))
			charset = StandardCharsets.UTF_8;
		else
			charset = Charset.forName(characterSet);

		String contentType = "application/soap+xml";

		log.debug("Calling webservice on URI [" + URL + "]");
		log.debug("        Time out            [" + timeout + "]");
		log.debug("        Connection Time out [" + connectionTimeout + "]");
		log.debug("        Socket Time out     [" + soTimeout + "]");
		log.debug("        Character Set       [" + charset.name() + "]");
		log.debug("        Soap Action         [" + soapAction + "]");
		log.debug("        Content type        [" + contentType + "]");

		HttpPost httpPost = new HttpPost(URL);
		HttpEntity entity = null;
		String response = null;
		CloseableHttpClient client = null;

		if (characterSet != null)
			contentType = contentType + "charset=" + characterSet;

		try {
			if (characterSet != null) {
				entity = new StringEntity(request, characterSet);
			} else {
				entity = new ByteArrayEntity(request.getBytes());
			}

			httpPost.setHeader("Content-Type", contentType);
			httpPost.setEntity(entity);
			httpPost.setHeader("action", soapAction);
			httpPost.setHeader("SOAPAction", soapAction);

			if (!Utils.emptyMap(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
					log.debug("        " + entry.getKey() + "        [" + entry.getValue() + "]");
				}
			}

			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionTimeout)
					.setMaxRedirects(1).setSocketTimeout(soTimeout).setConnectTimeout(timeout).build();
			httpPost.setConfig(config);

			HttpClientBuilder builder = HttpClientBuilder.create();

			builder.disableCookieManagement().useSystemProperties().setDefaultRequestConfig(config);

			if (trustAllCerts && URL.startsWith("https")) {
				builder.setSslcontext(noopContext);
			}

			if (trustAllCerts) {
				builder.setHostnameVerifier(customHostnameVerififier);
			}

			client = builder.build();

			Response rsp = performMonitoredHttpCall(client, httpPost, soTimeout);
			response = rsp.response;
			HttpResponse httpResponse = rsp.httpResponse;

			log.debug("http status code [" + httpResponse.getStatusLine().getStatusCode() + "]");

			if (httpResponse.getStatusLine().getStatusCode() < HttpStatus.SC_OK
					|| httpResponse.getStatusLine().getStatusCode() > HttpStatus.SC_PARTIAL_CONTENT) {

				result.setCode(SystemError.UNKNOWN_ERROR.getErrorCode());
				result.setType(ErrorType.EXTERNAL_SYSTEM_ERROR);
			}
			result.setData(response, String.class);

		} catch (Exception e) {
			ExceptionUtils.callExceptionToSystemException(e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					log.error("Unable to close the client", e);
				}
				client = null;
			}
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (UnsupportedOperationException e) {
					log.warn("Failed freeing http entity", e);
				} catch (IOException e) {
					log.warn("Failed freeing http entity", e);
				}
			}

		}
		wp.stop();

		return result;
	}

	/**
	 * Sends a request to the URL specified and returns the response using the UTF-8
	 * encoding
	 * 
	 * @param URL               URL to sent the request to
	 * @param request           request to send
	 * @param timeout           time out for the request
	 * @param connectionTimeout connection time-out for the request
	 * @param soTimeout         socket time out
	 * @return Response string
	 * @throws SystemException
	 */
	public static Result getResponseAsJson(String URL, String request, int timeout, int connectionTimeout,
			int soTimeout) throws SystemException {

		return getResponseAsJson(URL, request, timeout, connectionTimeout, soTimeout, null, null, true, false);
	}

	/**
	 * Sends a request to the URL specified and returns the response. Supports
	 * specifying the character set to be used
	 * 
	 * @param URL                URL to sent the request to
	 * @param request            request to send
	 * @param timeout            time out for the request
	 * @param connectionTimeout  connection time-out for the request
	 * @param soTimeout          socket time out
	 * @param characterSet       character set to be used
	 * @param trustAllCerts
	 * @param headerRequestIdTag
	 * @return
	 * @throws SystemException
	 */
	public static Result getResponseAsJson(String URL, String request, int timeout, int connectionTimeout,
			int soTimeout, String characterSet, Map<String, String> headers, boolean isPost, boolean trustAllCerts)
			throws SystemException {

		SSLContext sslContext = null;
		boolean ignoreHostnameVerification = false;

		if (trustAllCerts && URL.startsWith("https")) {
			sslContext = noopContext;
			ignoreHostnameVerification = true;
		}

		return getResponseAsJson(URL, request, timeout, connectionTimeout, soTimeout, characterSet, headers, isPost,
				sslContext, ignoreHostnameVerification);
	}

	/**
	 * Sends a request to the URL specified and returns the response. Supports
	 * specifying the character set to be used
	 * 
	 * @param URL                URL to sent the request to
	 * @param request            request to send
	 * @param timeout            time out for the request
	 * @param connectionTimeout  connection time-out for the request
	 * @param soTimeout          socket time out
	 * @param characterSet       character set to be used
	 * @param trustAllCerts
	 * @param headerRequestIdTag
	 * @return
	 * @throws SystemException
	 */
	public static Result getResponseAsJson(String URL, String request, int timeout, int connectionTimeout,
			int soTimeout, String characterSet, Map<String, String> headers, boolean isPost, boolean trustAllCerts,
			KeyManagerFactory keyFactoryManager) throws SystemException {

		SSLContext sslContext = null;
		boolean ignoreHostnameVerification = trustAllCerts;
		boolean isHttps = URL.startsWith("https");

		if (trustAllCerts && isHttps && keyFactoryManager == null) {
			sslContext = noopContext;
		} else if (isHttps && keyFactoryManager != null) {
			try {
				sslContext = SSLContext.getInstance("ssl");
				sslContext.init(keyFactoryManager.getKeyManagers(), trustAllCerts ? noopTrustManager : null,
						new java.security.SecureRandom());
			} catch (KeyManagementException | NoSuchAlgorithmException ex) {
				log.error("Error initiating ssl context");
				sslContext = null;
			}
		}
		return getResponseAsJson(URL, request, timeout, connectionTimeout, soTimeout, characterSet, headers, isPost,
				sslContext, ignoreHostnameVerification);
	}

	/**
	 * Sends a request to the URL specified and returns the response. Supports
	 * specifying the character set to be used
	 * 
	 * @param URL                URL to sent the request to
	 * @param request            request to send
	 * @param timeout            time out for the request
	 * @param connectionTimeout  connection time-out for the request
	 * @param soTimeout          socket time out
	 * @param characterSet       character set to be used
	 * @param trustAllCerts
	 * @param headerRequestIdTag
	 * @return
	 * @throws SystemException
	 */
	public static Result getResponseAsJson(String URL, String request, int timeout, int connectionTimeout,
			int soTimeout, String characterSet, Map<String, String> headers, boolean isPost, SSLContext sslContext,
			boolean ignoreHostnameVerification) throws SystemException {

		WatchPuppy wp = new WatchPuppy("JSON Service Call");

		Result result = Result.generate(SystemError.SUCCESS);

		Charset charset = null;
		if (StringUtils.isNotBlank(characterSet))
			charset = Charset.forName(characterSet);

		log.debug("Calling json service on URI [" + URL + "]");
		log.debug("        Time out            [" + timeout + "]");
		log.debug("        Connection Time out [" + connectionTimeout + "]");
		log.debug("        Socket Time out     [" + soTimeout + "]");
		if (charset != null)
			log.debug("        Character Set       [" + charset.name() + "]");

		HttpRequestBase httpRequest;

		if (isPost)
			httpRequest = new HttpPost(URL);
		else
			httpRequest = new HttpGet(URL);

		StringEntity entity = null;
		String response = null;
		CloseableHttpClient client = null;
		String uuid = null;
		try {
			if (characterSet != null) {
				entity = new StringEntity(request, characterSet);
				httpRequest.setHeader("Content-Type", "application/json;charset=" + characterSet);
			} else {
				entity = new StringEntity(request);
				httpRequest.setHeader("Content-Type", "application/json");
			}

			httpRequest.setHeader("Accept", "application/json");

			if (!Utils.emptyMap(headers)) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpRequest.setHeader(entry.getKey(), entry.getValue());
					log.debug("        " + entry.getKey() + "        [" + entry.getValue() + "]");
				}
			}

			if (isPost)
				((HttpPost) httpRequest).setEntity(entity);

			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionTimeout)
					.setMaxRedirects(1).setSocketTimeout(soTimeout).setConnectTimeout(timeout).build();

			httpRequest.setConfig(config);

			HttpClientBuilder builder = HttpClientBuilder.create();

			builder.disableCookieManagement().useSystemProperties().setDefaultRequestConfig(config);

			if (sslContext != null) {
				builder.setSslcontext(sslContext);
			}

			if (ignoreHostnameVerification) {
				builder.setHostnameVerifier(customHostnameVerififier);
			}

			client = builder.build();

			Response rsp = performMonitoredHttpCall(client, httpRequest, soTimeout);
			response = rsp.response;
			HttpResponse httpResponse = rsp.httpResponse;

			log.debug("http status code [" + httpResponse.getStatusLine().getStatusCode() + "]");

			if (httpResponse.getStatusLine().getStatusCode() < HttpStatus.SC_OK
					|| httpResponse.getStatusLine().getStatusCode() > HttpStatus.SC_PARTIAL_CONTENT) {

				result.setCode(SystemError.UNKNOWN_ERROR.getErrorCode());
				result.setType(ErrorType.EXTERNAL_SYSTEM_ERROR);
			}
			result.setMessage(uuid);
			result.setData(response, String.class);

		} catch (Exception e) {
			ExceptionUtils.callExceptionToSystemException(e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					log.error("Unable to close the client", e);
				}
				client = null;
			}
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (UnsupportedOperationException e) {
					log.warn("Failed freeing http entity", e);
				} catch (IOException e) {
					log.warn("Failed freeing http entity", e);
				}
			}

		}
		wp.stop();

		return result;
	}

	static class Response {
		String response;
		HttpResponse httpResponse;
		String threadName;
	}

	/**
	 * performed time-bounded call
	 * 
	 * @param client
	 * @param httpRequest
	 * @param timeout
	 * @return
	 * @throws SystemException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static Response performMonitoredHttpCall(CloseableHttpClient client, HttpRequestBase httpRequest,
			int timeout) throws SystemException, ClientProtocolException, IOException {

		Response response = null;

		if (executor == null) {
			HttpResponse httpResponse = client.execute(httpRequest);
			response = new Response();
			response.httpResponse = httpResponse;
			response.response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		} else {

			final Future<Response> future = executor.submit(() -> {
				log.debug("calling async");
				HttpResponse httpResponse = client.execute(httpRequest);
				Response rsp = new Response();
				rsp.httpResponse = httpResponse;
				rsp.threadName = Thread.currentThread().getName();
				rsp.response = EntityUtils.toString(httpResponse.getEntity());
				return rsp;
			});

			try {
				response = future.get(timeout, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				log.error("Failed calling webservice", e);
				throw new SystemException(SystemError.SE_TIME_OUT, "Middleware service call");
			} catch (Exception e) {
				log.error("Failed calling webservice", e);
				throw new SystemException(SystemError.SE_SYSTEM_ERROR, e.getMessage());
			} finally {
				future.cancel(true);
			}

			log.debug("Execusion finished from [" + response.threadName + "]");
		}

		return response;
	}

	public static void setMaximumNumberOfThreads(int threads) {
		if (threads > 1) {
			log.debug("Initializing Async Executor");
			executor = Executors.newFixedThreadPool(threads);
		}
	}

	public static void shutdownExecusion() {
		if (executor != null)
			executor.shutdown();
		executor = null;
	}

	/**
	 * Sends a request to the URL specified and returns the response using the UTF-8
	 * encoding
	 * 
	 * @param URL               URL to sent the request to
	 * @param request           request to send
	 * @param timeout           time out for the request
	 * @param connectionTimeout connection time-out for the request
	 * @param soTimeout         socket time out
	 * @return Response string
	 * @throws SystemException
	 */
	public static String getResponseByXML(String URL, String request, int timeout, int connectionTimeout, int soTimeout)
			throws SystemException {

		return getResponseByXML(URL, request, timeout, connectionTimeout, soTimeout, HTTP.UTF_8);
	}

	/**
	 * Sends a request to the URL specified and returns the response. Supports
	 * specifying the character set to be used
	 * 
	 * @param URL               URL to sent the request to
	 * @param request           request to send
	 * @param timeout           time out for the request
	 * @param connectionTimeout connection time-out for the request
	 * @param soTimeout         socket time out
	 * @param characterSet      character set to be used
	 * @return Response string
	 * @throws SystemException
	 */
	public static String getResponseByXML(String URL, String request, int timeout, int connectionTimeout, int soTimeout,
			String characterSet) throws SystemException {

		WatchPuppy wp = new WatchPuppy("Service Call");

		if (StringUtils.isBlank(characterSet))
			characterSet = HTTP.UTF_8;

		log.debug("Calling webservice on URI [" + URL + "]");
		log.debug("        Time out            [" + timeout + "]");
		log.debug("        Connection Time out [" + connectionTimeout + "]");
		log.debug("        Socket Time out     [" + soTimeout + "]");
		log.debug("        Character Set       [" + characterSet + "]");

		HttpPost httpPost = new HttpPost(URL);
		StringEntity entity = null;
		String response = null;
		HttpClient client = null;
		try {
			entity = new StringEntity(request, characterSet);
			httpPost.setHeader("Content-Type", "text/xml;charset=" + characterSet);
			httpPost.setEntity(entity);
			HttpParams httpParams = new BasicHttpParams();
			ConnManagerParams.setTimeout(httpParams, timeout);
			HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
			client = new DefaultHttpClient(httpParams);
			client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpResponse httpResponse = client.execute(httpPost);
			response = EntityUtils.toString(httpResponse.getEntity());

		} catch (Exception e) {
			ExceptionUtils.callExceptionToSystemException(e);
		} finally {
			if (client != null && client.getConnectionManager() != null) {
				client.getConnectionManager().shutdown();
				client = null;
			}
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (UnsupportedOperationException e) {
					log.warn("Failed freeing http entity", e);
				} catch (IOException e) {
					log.warn("Failed freeing http entity", e);
				}
			}

		}
		wp.stop();

		return response;

	}
}
