package com.pchome.soft.depot.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * httpclient all settings
 *
 * @author supertolove
 */
public class HttpUtil {
	private static HttpUtil http = new HttpUtil();
	private DefaultHttpClient client;
	private static final Log log = LogFactory.getLog(HttpUtil.class);

	public synchronized DefaultHttpClient getClient() {
		return client;
	}

	private HttpUtil() {
		configureClient();
	}

	public static HttpUtil getInstance() {
		return http;
	}

	public synchronized String getResult(String url, String charSet) {
	    int statusCode = 0;
        String result = null;

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        HttpGet httpget = null;

        log.debug(url);
        try {
            httpget = new HttpGet(url);
            response = client.execute(httpget);
            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity,charSet);
            } else {
                log.info("statusCode=" + statusCode);
            }
        } catch (Exception e) {
            log.error(e.toString() + " " + url);
        } finally {
            httpget.abort();
            closeExpiredConns();
            closeIdleConns();
        }

        result = StringUtils.defaultIfEmpty(result, "");
        return result;
    }

	public String getResultNoHtml(String url, String charSet) {
        String result = null;

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        HttpGet httpget = null;

        log.debug(url);
        try {
            httpget = new HttpGet(url);
            response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,charSet);
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            httpget.abort();
            closeExpiredConns();
            closeIdleConns();
        }

        if(result==null){
        	result="";
        }

        result=NoHTML(result);
        result = StringUtils.defaultIfEmpty(result, "");
        return result;
    }

	private String NoHTML(String Htmlstring) {
		//删除脚本
		//String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
       // String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
       // String regEx_html = "<[^>]+>"; //定義HTML標籤的正規表示式
        Htmlstring = Htmlstring.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        Htmlstring = Htmlstring.replaceAll("<[\\s]*?iframe[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?iframe[\\s]*?>", "");
        Htmlstring = Htmlstring.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        Htmlstring = Htmlstring.replaceAll("<[^>]+>", "");
        Htmlstring = Htmlstring.replaceAll("&nbsp;", "");
       // Htmlstring = Htmlstring.replaceAll("[ ]*", "");

		//Htmlstring = Htmlstring.replaceAll("<script[^<>]*>(.*?)</script>", "");
		//删除HTML

		Htmlstring = Htmlstring.replaceAll("<a.*?</a>", "");
		Htmlstring = Htmlstring.replaceAll("<(.[^>]*)>", "");
		Htmlstring = Htmlstring.replaceAll("([\r\n])+", "");
		Htmlstring = Htmlstring.replaceAll("-->", "");
		Htmlstring = Htmlstring.replaceAll("<!--.*", "");
		Htmlstring = Htmlstring.replaceAll("&(quot|#34);", "\"");
		Htmlstring = Htmlstring.replaceAll("&(amp|#38);", "&");
		Htmlstring = Htmlstring.replaceAll("&(lt|#60);", "<");
		Htmlstring = Htmlstring.replaceAll("&(gt|#62);", ">");
		Htmlstring = Htmlstring.replaceAll("&(nbsp|#160);", " ");
		Htmlstring = Htmlstring.replaceAll("&(iexcl|#161);", "");
		Htmlstring = Htmlstring.replaceAll("&(cent|#162);", "");
		Htmlstring = Htmlstring.replaceAll("&(pound|#163);", "");
		Htmlstring = Htmlstring.replaceAll("&(copy|#169);", "");

		Htmlstring = Htmlstring.replaceAll("<", "");
		Htmlstring = Htmlstring.replaceAll(">", "");
		Htmlstring = Htmlstring.replaceAll("\r\n", "");
		Htmlstring = Htmlstring.replaceAll("[ ]*", "");

		return Htmlstring;
	}

	public synchronized String getResultPost(String url, String keyvalue,String charSet) {
		String result = null;

		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

		HttpPost post = new HttpPost(url);

		String data[]=keyvalue.split(",");
		String value[];

		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		for(String s:data){
			value=s.split("_");
			nvps.add(new BasicNameValuePair(value[0], value[1]));
		}

		try {
			post.setEntity(new UrlEncodedFormEntity(nvps,charSet));
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = StringUtils.defaultIfEmpty(result, "");
		return result;
	}

	private void configureClient() {
		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();

        // 設定連接超時
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
        HttpConnectionParams.setSoTimeout(params, 30 * 1000);

        HttpProtocolParams.setUserAgent(params, "PChome Search");

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);

		// Increase max total connection to 200
		cm.setMaxTotal(1050);

		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(100);

		// Increase max connections for localhost:80 to 50
//		HttpHost localhost = new HttpHost("localhost", 8080);
//		cm.setMaxForRoute(new HttpRoute(localhost), 100);

		client = new DefaultHttpClient(cm);
		client.setParams(params);
		client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
	}

	
	public void shutdownHttp() {
		client.getConnectionManager().shutdown();
	}

	public void closeExpiredConns() {
		client.getConnectionManager().closeExpiredConnections();
	}

	public void closeIdleConns() {
		client.getConnectionManager().closeIdleConnections(0L, TimeUnit.SECONDS);
	}


}
