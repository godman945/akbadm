package com.pchome.akbadm.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

public class HttpUtil {

	private static final Logger logger = LogManager.getRootLogger();

	public static String doGet(String url, String queryString) throws Exception {
		String result = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        try {

        	if (StringUtils.isNotEmpty(queryString)) {
        		method.setQueryString(URIUtil.encodeQuery(queryString, "utf-8"));
        	}

        	int returnCode = client.executeMethod(method);
        	System.out.println(">>> returnCode = " + returnCode);
        	System.out.println(">>> response = " + method.getResponseBodyAsString());

        	if (returnCode == HttpStatus.SC_OK) {
        		result = method.getResponseBodyAsString();
        	}

        } catch (URIException urie) {
        	logger.error(urie);
        	throw urie;
        } catch (IOException ioe) {
        	logger.error(ioe);
        	throw ioe;
        } catch (Exception e) {
        	logger.error(e);
        	throw e;
        } finally {
        	method.releaseConnection();
        }

        return result;
	}

	public static String doPost(String url, Map<String, String> paramsMap) throws Exception {
		String result = null;

		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"); 

		try {

			if (paramsMap != null) {
				for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
					method.addParameter(entry.getKey(), entry.getValue());
				}
			}

			int returnCode = new HttpClient().executeMethod(method);
			System.out.println(">>> returnCode = " + returnCode);
			System.out.println(">>> response = " + method.getResponseBodyAsString());

			if (returnCode == HttpStatus.SC_OK) {
				result = method.getResponseBodyAsString();
			}

        } catch (URIException urie) {
        	logger.error(urie);
        	throw urie;
        } catch (IOException ioe) {
        	logger.error(ioe);
        	throw ioe;
        } catch (Exception e) {
        	logger.error(e);
        	throw e;
        } finally {
        	method.releaseConnection();
        }

		return result;
	}

	public static String doPost(String url, JSONObject jsonObjectData) throws Exception {
		String result = null;

		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"); 

		try {
			Iterator jsonObjectIterator = jsonObjectData.keys();
			while (jsonObjectIterator.hasNext()) {
				String key = jsonObjectIterator.next().toString();
				method.addParameter(key, jsonObjectData.optString(key));
			}
			
			int returnCode = new HttpClient().executeMethod(method);
			System.out.println(">>> returnCode = " + returnCode);
			System.out.println(">>> response = " + method.getResponseBodyAsString());

			if (returnCode == HttpStatus.SC_OK) {
				result = method.getResponseBodyAsString();
			}

        } catch (URIException urie) {
        	logger.error(urie);
        	throw urie;
        } catch (IOException ioe) {
        	logger.error(ioe);
        	throw ioe;
        } catch (Exception e) {
        	logger.error(e);
        	throw e;
        } finally {
        	method.releaseConnection();
        }

		return result;
	}
	
	/**
	 * 略過https驗證
	 */
	public static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {}
		} };
		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) {

		try {

//			String url = "http://billingstg.pchome.com.tw/membervip/updateMemberStatusAPI.html";
//			Map<String, String> paramsMap = new HashMap<String, String>();
//			paramsMap.put("email", "baluball@gmail.com");
//			paramsMap.put("status", "0");
//			paramsMap.put("reason", "恢復");
//			paramsMap.put("updateUser", "admin");
//			String postResult = HttpUtil.doPost(url, paramsMap);
//			System.out.println(">>> postResult = " + postResult);

			/*
			String url2 = "http://billingstg.pchome.com.tw/membervip/MemberInfoAPI.html";
			String getResult = HttpUtil.doGet(url2, "rocId=A999888777");
			System.out.println(">>> getResult = " + getResult);
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
