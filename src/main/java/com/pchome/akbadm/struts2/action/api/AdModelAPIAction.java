package com.pchome.akbadm.struts2.action.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.pchome.akbadm.factory.ad.AdFactory;
import com.pchome.akbadm.factory.ad.AdModelUtil;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.api.IAPIProvider;


public class AdModelAPIAction extends BaseCookieAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1154356159039269532L;

	private IAPIProvider admAPI;

	private AdFactory adFactory;
	private String adNo;
	private String tproNo;
	private String adPreviewVideoURL;
	private String adPreviewVideoBgImg="";
	private String realUrl = "";
	// 回傳廣告Html內容
	private InputStream inputAdModel;


	public String getHtmlContent() throws Exception{
		
		String adModel = adFactory.getAdModel(tproNo, adNo);
		// 回傳廣告內容
		inputAdModel = new ByteArrayInputStream(adModel.getBytes("UTF-8"));
		
		
		return SUCCESS;
	}

	public String adModelAction() throws Exception{
		
		log.info(" tproNo = "+tproNo+"  , adNo = "+adNo);

		String adHtml = adFactory.getAdModel(tproNo, adNo);
		//String adHtml = admAPI.getAdContent(tproNo, adNo);

		log.info(" adHtml = "+adHtml);
		
		inputAdModel = new ByteArrayInputStream(adHtml.getBytes("UTF-8"));	 
		
		return SUCCESS;
	}

	/**
	 * 吐影音廣告
	 */
	public String adModelVideoAction() throws Exception{
	    String adHtml = adFactory.getAdModelUtil().getAdVideoModel(adPreviewVideoURL,adPreviewVideoBgImg,realUrl);
	    inputAdModel = new ByteArrayInputStream(adHtml.getBytes("UTF-8"));
	    return SUCCESS;
	}
	
	public void setAdFactory(AdFactory adFactory) {
		this.adFactory = adFactory;
	}

	public void setAdNo(String adNo) {
		this.adNo = adNo;
	}
	
	public void setTproNo(String tproNo) {
		this.tproNo = tproNo;
	}
	
	public InputStream getInputAdModel() {
		return inputAdModel;
	}

	public String getAdPreviewVideoURL() {
		return adPreviewVideoURL;
	}

	public void setAdPreviewVideoURL(String adPreviewVideoURL) {
		this.adPreviewVideoURL = adPreviewVideoURL;
	}

	public String getAdPreviewVideoBgImg() {
		return adPreviewVideoBgImg;
	}

	public void setAdPreviewVideoBgImg(String adPreviewVideoBgImg) {
		this.adPreviewVideoBgImg = adPreviewVideoBgImg;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}



}
