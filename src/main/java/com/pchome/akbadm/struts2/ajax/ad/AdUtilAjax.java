package com.pchome.akbadm.struts2.ajax.ad;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.pchome.akbadm.struts2.BaseCookieAction;
//import com.pchome.akbpfp.struts2.BaseCookieAction;
//import com.pchome.enumerate.ad.EnumAdVideoCondition;
import com.pchome.soft.depot.utils.HttpUtil;

public class AdUtilAjax extends BaseCookieAction{

	private static final long serialVersionUID = -5195311542239203862L;
	// get data
	private String url;
	private String q;
	// return data
	private InputStream msg;
	private int urlState;
	private String result;
	private String akbPfpServer;
	private String adVideoUrl;
	
	/**
	 * 檢查輸入的顯示廣告網址，確認是否為危險網址
	 * @param adShowUrl
	 * @param akbPfpServer 
	 * @return
	 * @throws Exception
	 */
	public String checkAdShowUrl(String adShowUrl, String akbPfpServer) throws Exception {
		url = adShowUrl;
		this.akbPfpServer = akbPfpServer;
		checkAdUrl();
		if (urlState < 200 || urlState >= 300) {
			return "請輸入正確的廣告顯示網址";
		}
		return "";
	}
	
	/**
	 * 檢查輸入的廣告網址，確認是否為危險網址
	 * @return
	 * @throws Exception
	 */
	public String checkAdUrl() throws Exception{
		log.info(">>>>>> START checkAdUrl");
		Boolean noError = false;
		//檢查url 是否危險網址API
		HttpGet request = new HttpGet();
		URL thisUrl = new URL("http://pseapi.mypchome.com.tw/api/security/safeBrowsingLookup.html?url=" + url);
		URI uri = new URI(thisUrl.getProtocol(), thisUrl.getHost(), thisUrl.getPath(), thisUrl.getQuery(), null);
		request.setURI(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		InputStream urlContent = response.getEntity().getContent();
		BufferedReader buf = new BufferedReader(new InputStreamReader(urlContent, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String jsonStr;
		while (true) {
			jsonStr = buf.readLine();
			if (jsonStr == null){
				break;
			}
			sb.append(jsonStr);
		}
		buf.close();
		urlContent.close();
		JSONObject jsonObj = new JSONObject(sb.toString());
		JSONObject jsonObjMsg = new JSONObject(jsonObj.get("success").toString());
		if (jsonObjMsg.get("msg").toString().equals("malware")) {
			this.msg = new ByteArrayInputStream(jsonObjMsg.get("msg").toString().getBytes());
			return SUCCESS;
		} else {
			try {
				if (StringUtils.isEmpty(url) || url.length() < 1) {
					return null;
				} else {
					if (url.indexOf("http") < 0) {
						url = "http://" + url;
					}
				}
				url = HttpUtil.getInstance().getRealUrl(url);
			    // www.mjholly.com pass
				String passUrl = url;
				if (url.indexOf("www.mjholly.com") >= 0) {
					passUrl = passUrl.replaceAll("http://", "");
					passUrl = passUrl.replaceAll("https://", "");
					passUrl = passUrl.substring(0, passUrl.indexOf(".com") + 4);
				}
			    
				if (url.indexOf(akbPfpServer) == 0) {
			    //if(akbPfpServer.equals(url) || (akbPfpServer.substring(0, akbPfpServer.length() -1).equals(url))){
					urlState = 200;
				} else if ("www.mjholly.com".equals(passUrl)) {
					urlState = 200;
				} else {
					urlState = HttpUtil.getInstance().getStatusCode(url);
					msg = new ByteArrayInputStream("".getBytes());
				}
				if (urlState >= 200 && urlState < 300) {
					noError = true;
				}
			} catch (Exception ex) {
				log.info("Exception(AdUtilAjax.checkUrl) : " + ex.toString());
			}
			
			if (url != null && !url.trim().equals("")) {
				msg = new ByteArrayInputStream(noError.toString().getBytes());
			} else {
				msg = new ByteArrayInputStream("".getBytes());
			}
		}
		log.info(">>>>>> END checkAdUrl");
		return SUCCESS;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public InputStream getMsg() {
		return msg;
	}

	public int getUrlState() {
		return urlState;
	}

	public String getResult() {
		return result;
	}

	public void setAkbPfpServer(String akbPfpServer) {
		this.akbPfpServer = akbPfpServer;
	}

	public String getAdVideoUrl() {
		return adVideoUrl;
	}

	public void setAdVideoUrl(String adVideoUrl) {
		this.adVideoUrl = adVideoUrl;
	}

}
