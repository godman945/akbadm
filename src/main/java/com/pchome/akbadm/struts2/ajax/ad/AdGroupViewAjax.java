package com.pchome.akbadm.struts2.ajax.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.ad.IPfpAdGroupService;
import com.pchome.akbadm.db.vo.ad.PfpAdGroupViewVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdSearchPriceType;

public class AdGroupViewAjax extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IPfpAdGroupService pfpAdGroupService;
	
	private String startDate;
	private String endDate;
	private String dateType;
	//會員帳號
	private String userAccount;
	private String searchAdStatus;
	//廣告類型
	private String searchType;
	//分類名稱
	private String keyword;
	private String adActionSeq;
	//裝置
	private String adPvclkDevice;
	private List<PfpAdGroupViewVO> adGroupViewVO;
	private String adclkDevice;
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 20;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private int totalCount = 0;   				// 初始化共幾筆
	
	private EnumAdSearchPriceType[] adSearchPriceType;
	private int totalSize = 0;						
	private int totalPv = 0;						
	private int totalClk = 0;						
	private float totalClkRate = 0;
	private float totalAvgCost = 0;
	private int totalCost = 0;
	private int totalInvalidClk = 0;
	private int totalInvalidClkPrice = 0;
	private boolean changeSelect;
	
	public String adGroupViewTableAjax() throws Exception{
	    adSearchPriceType = EnumAdSearchPriceType.values();
	    if(!changeSelect && !StringUtils.isEmpty(adclkDevice)){
		adPvclkDevice = adclkDevice;
	    }
	    Map<String,String> adGroupConditionMap =new HashMap<String,String>();
	    adGroupConditionMap.put("startDate", startDate);
	    adGroupConditionMap.put("endDate", endDate);
	    adGroupConditionMap.put("dateType",dateType);
	    adGroupConditionMap.put("userAccount",userAccount);
	    adGroupConditionMap.put("adType",searchType);
	    adGroupConditionMap.put("adPvclkDevice",adPvclkDevice);
	    adGroupConditionMap.put("pageNo",String.valueOf(pageNo));
	    adGroupConditionMap.put("pageSize",String.valueOf(pageSize));
	    adGroupConditionMap.put("adGroupName",keyword);
	    adGroupConditionMap.put("adGroupStatus",searchAdStatus);
	    adGroupConditionMap.put("adActionSeq",adActionSeq);
	    adGroupConditionMap.put("adclkDevice",adclkDevice);
	    adGroupConditionMap.put("changeSelect",String.valueOf(changeSelect));
	    adGroupViewVO = pfpAdGroupService.getAdGroupReport(adGroupConditionMap);
	    int adReportTotalSize = pfpAdGroupService.getAdGroupReportSize(adGroupConditionMap);
	    totalSize = adReportTotalSize;
	    totalCount = adReportTotalSize;
	    pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    if(adGroupViewVO != null){
	    	for(PfpAdGroupViewVO vo:adGroupViewVO){
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
	
//	public String adGroupSuggestPriceAjax() throws Exception{
//
//		PfpAdSysprice adSysprice = pfpAdSyspriceService.getAdSysprice(adPoolSeq);
//
//		sysprice = adSysprice.getSysprice();
//		
//		adAsideRate = syspriceOperaterAPI.getAdAsideRate(userprice);
//
//		
//		return SUCCESS;
//	}
	

	public void setPfpAdGroupService(IPfpAdGroupService pfpAdGroupService) {
		this.pfpAdGroupService = pfpAdGroupService;
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

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAdActionSeq() {
		return adActionSeq;
	}
	
	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}
	public String getAdPvclkDevice() {
		return adPvclkDevice;
	}

	public void setAdPvclkDevice(String adPvclkDevice) {
		this.adPvclkDevice = adPvclkDevice;
	}

	public List<PfpAdGroupViewVO> getAdGroupViewVO() {
		return adGroupViewVO;
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

	public EnumAdSearchPriceType[] getAdSearchPriceType() {
		return adSearchPriceType;
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

	public String getAdclkDevice() {
	    return adclkDevice;
	}

	public void setAdclkDevice(String adclkDevice) {
	    this.adclkDevice = adclkDevice;
	}

	public boolean isChangeSelect() {
	    return changeSelect;
	}

	public void setChangeSelect(boolean changeSelect) {
	    this.changeSelect = changeSelect;
	}
	
}
