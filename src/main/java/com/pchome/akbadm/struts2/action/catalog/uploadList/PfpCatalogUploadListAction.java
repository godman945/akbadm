package com.pchome.akbadm.struts2.action.catalog.uploadList;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;

import com.pchome.akbadm.db.service.catalog.uploadList.IPfpCatalogUploadListService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class PfpCatalogUploadListAction extends BaseCookieAction{
	
	protected Logger log = LogManager.getRootLogger();
	
	private Map<String,Object> dataMap;
	private IPfpCatalogUploadListService pfpCatalogUploadListService;

	// pfp手動上傳api用
	private String catalogSeq;
	private String catalogType;
	private String updateWay;
	private String catalogUploadType;
	private String updateContent;
	private String pfpCustomerInfoId;
	private String catalogProdItem;
	
	/**
	 * 廣告商品資料手動上傳api
	 * @return
	 * @throws Exception
	 */
	public String processCatalogProdJsonDataApi() throws Exception {
		dataMap = new HashMap<String, Object>();
		
		JSONObject apiJsonData = new JSONObject();
		apiJsonData.put("catalogSeq", catalogSeq);
		apiJsonData.put("catalogType", catalogType);
		apiJsonData.put("updateWay", updateWay);
		apiJsonData.put("catalogUploadType", catalogUploadType);
		apiJsonData.put("updateContent", updateContent);
		apiJsonData.put("pfpCustomerInfoId", pfpCustomerInfoId);
		apiJsonData.put("catalogProdItem", catalogProdItem);

		dataMap = pfpCatalogUploadListService.processCatalogProdJsonData(apiJsonData);
		
		return SUCCESS;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setPfpCatalogUploadListService(IPfpCatalogUploadListService pfpCatalogUploadListService) {
		this.pfpCatalogUploadListService = pfpCatalogUploadListService;
	}

	public void setCatalogSeq(String catalogSeq) {
		this.catalogSeq = catalogSeq;
	}

	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}

	public void setUpdateWay(String updateWay) {
		this.updateWay = updateWay;
	}

	public void setCatalogUploadType(String catalogUploadType) {
		this.catalogUploadType = catalogUploadType;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}

	public void setCatalogProdItem(String catalogProdItem) {
		this.catalogProdItem = catalogProdItem;
	}

}