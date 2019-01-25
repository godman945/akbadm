package com.pchome.akbadm.utils;

import java.util.*;

import java.io.*;

import org.apache.commons.httpclient.HttpClient; 
import org.apache.commons.httpclient.HttpStatus; 
import org.apache.commons.httpclient.URIException; 
import org.apache.commons.httpclient.methods.GetMethod; 
import org.apache.commons.httpclient.methods.PostMethod; 
import org.apache.commons.httpclient.util.URIUtil; 

import org.apache.commons.lang.StringUtils; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {

	private static final Log logger = LogFactory.getLog(HttpUtil.class);

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
