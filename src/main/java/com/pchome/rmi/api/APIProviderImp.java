package com.pchome.rmi.api;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.factory.ad.AdFactory;

public class APIProviderImp implements IAPIProvider{
	protected Logger log = LogManager.getRootLogger();
	private AdFactory adFactory;
	
	public String getAdContent(String tproNo, String adNo) throws Exception{
		return adFactory.getAdModel(tproNo, adNo);
	}
	
	public String getAdVideoContent(String adPreviewVideoURL,String adPreviewVideoBgImg,String realUrl) throws Exception {
		log.info(">>>>>>>>>>>>>>>>>>>>adPreviewVideoURL:"+adPreviewVideoURL);
		log.info(">>>>>>>>>>>>>>>>>>>>adPreviewVideoBgImg:"+adPreviewVideoBgImg);
		log.info(">>>>>>>>>>>>>>>>>>>>realUrl:"+realUrl);
		return adFactory.getAdModelUtil().getAdVideoModel(adPreviewVideoURL,adPreviewVideoBgImg,realUrl);
	}
	
	public String adProdContent(String pfpProdAdPreviewJson) throws Exception {
		return adFactory.getAdModelUtil().adProdModelPreview(pfpProdAdPreviewJson);
	}
	
	public String lifeCheck() {
		return "OK";
	}
	
	public void setAdFactory(AdFactory adFactory) {
		this.adFactory = adFactory;
	}

	public AdFactory getAdFactory() {
		return adFactory;
	}

}
