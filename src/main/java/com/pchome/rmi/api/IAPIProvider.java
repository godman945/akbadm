package com.pchome.rmi.api;

public interface IAPIProvider {
	
	/**
	 * 取Html廣告內容 
	 */
	public String getAdContent(String tproNo, String adNo) throws Exception;
	
	/**
	 * 取Video廣告內容 
	 */
	public String getAdVideoContent(String adPreviewVideoURL,String adPreviewVideoBgImg,String realUrl) throws Exception;
	
	/**
	 * 取Prod廣告內容 
	 */
	public String adProdContent(String pfpProdAdPreviewJson) throws Exception;
	
	/**
	 * 偵測 RMI 系統是否可運行
	 */
	public String lifeCheck();
}
