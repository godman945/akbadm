package com.pchome.akbadm.struts2.ajax.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.vo.ad.PfpAdActionViewVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdType;

public class AdActionViewAjax extends BaseCookieAction{

	private IPfpAdActionService pfpAdActionService;

	private String startDate;
	private String endDate;
	private String dateType;
	private String userAccount;
	private String searchAdStatus;
	private String searchType;
	private String adActionName;
	private String adPvclkDevice;
	private List<PfpAdActionViewVO> adActionViewVO;
	
	
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 20;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private int totalCount = 0;   				// 初始化共幾筆
	
	private int totalSize = 0;						
	private int totalPv = 0;						
	private int totalClk = 0;						
	private float totalClkRate = 0;
	private float totalAvgCost = 0;
	private int totalCost = 0;
	private int totalInvalidClk = 0;
	private int totalInvalidClkPrice = 0;
	
	public String execute() throws Exception{
		
		return SUCCESS;
	}

	
	/**
	 * dateType:日期類型
	 * userAccount:會員帳號
	 * adType:廣告類型
	 * adPvclkDevice:裝置
	 * pageNo:頁數
	 * pageSize:每頁筆數
	 * adActionName:廣告名稱
	 * searchAdStatus;廣告狀態
	 * */
	public String adActionViewTableAjax() throws Exception{
		int adType = 0;
		if(StringUtils.isNotEmpty(searchType)) {
			for(EnumAdType enumAdType:EnumAdType.values()){
				if(enumAdType.getType() == Integer.parseInt(searchType)){
					adType = enumAdType.getType();
				}
			}
		}
		Map<String,String> adActionConditionMap =new HashMap<String,String>();
		adActionConditionMap.put("adPvclkDevice",adPvclkDevice);
		adActionConditionMap.put("startDate", startDate);
		adActionConditionMap.put("endDate", endDate);
		adActionConditionMap.put("dateType",dateType);
		adActionConditionMap.put("userAccount",userAccount);
		adActionConditionMap.put("adType",String.valueOf(adType));
		adActionConditionMap.put("pageNo",String.valueOf(pageNo));
		adActionConditionMap.put("pageSize",String.valueOf(pageSize));
		adActionConditionMap.put("adActionName",adActionName);
		adActionConditionMap.put("searchAdStatus",searchAdStatus);
		adActionViewVO = pfpAdActionService.getAdActionReport(adActionConditionMap);
		int adReportTotalSize = pfpAdActionService.getAdActionReportSize(adActionConditionMap);
		totalSize = adReportTotalSize;
		totalCount = adReportTotalSize;
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		if(adActionViewVO != null){
			for(PfpAdActionViewVO vo:adActionViewVO){
				totalPv += vo.getAdPv();
				totalClk += vo.getAdClk();		
				totalCost += vo.getAdClkPrice();
				totalInvalidClk += vo.getAdInvalidClk();
				totalInvalidClkPrice += vo.getAdInvalidClkPrice();
			}
			if(totalClk > 0 || totalPv > 0){
				totalClkRate = (float)totalClk / (float)totalPv*100;
			}
			if(totalCost > 0 || totalClk > 0){
				totalAvgCost = (float)totalCost / (float)totalClk;	
			}
		}
		return SUCCESS;
	}
	

	public void setPfpAdActionService(IPfpAdActionService pfpAdActionService) {
		this.pfpAdActionService = pfpAdActionService;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setSearchAdStatus(String searchAdStatus) {
		this.searchAdStatus = searchAdStatus;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setAdActionName(String adActionName) {
		this.adActionName = adActionName;
	}

	public void setAdPvclkDevice(String adPvclkDevice) {
		this.adPvclkDevice = adPvclkDevice;
	}

	public List<PfpAdActionViewVO> getAdActionViewVO() {
		return adActionViewVO;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public int getTotalPv() {
		return totalPv;
	}

	public int getTotalClk() {
		return totalClk;
	}

	public float getTotalClkRate() {
		return totalClkRate;
	}

	public float getTotalAvgCost() {
		return totalAvgCost;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public int getTotalInvalidClk() {
		return totalInvalidClk;
	}

	public int getTotalInvalidClkPrice() {
		return totalInvalidClkPrice;
	}
	
	
}
