package com.pchome.akbadm.struts2.ajax.ad;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.ad.IPfpAdKeywordService;
import com.pchome.akbadm.db.vo.ad.PfpAdKeywordViewVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class AdKeywordViewAjax extends BaseCookieAction{
	
	private IPfpAdKeywordService pfpAdKeywordService;

	private String startDate;
	private String endDate;
	private String dateType;
	private String userAccount;
	private String searchAdStatus;
	private String searchType;
	private String keyword;
	private String adGroupSeq;
	private String adKeywordPvclkDevice;
	private List<PfpAdKeywordViewVO> adKeywordViewVO;
	private PfpAdKeywordViewVO totalVO;
	private String adclkDevice;
	
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 20;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private int totalCount = 0;   				// 初始化共幾筆
	
	private int totalSize = 0;						
	private int totalCost = 0;
	private boolean changeSelect;
	public String execute() throws Exception{
		
		return SUCCESS;
	}

	public String adKeywordViewTableAjax() throws Exception{
	    if(!changeSelect && !StringUtils.isEmpty(adclkDevice)){
		adKeywordPvclkDevice = adclkDevice;
	    }
	    Map<String,String> adKeywordViewConditionMap =new HashMap<String,String>();
	    adKeywordViewConditionMap.put("startDate", startDate);
	    adKeywordViewConditionMap.put("endDate", endDate);
	    adKeywordViewConditionMap.put("dateType",dateType);
	    adKeywordViewConditionMap.put("userAccount",userAccount);
	    adKeywordViewConditionMap.put("adType",searchType);
	    adKeywordViewConditionMap.put("adKeywordPvclkDevice",adKeywordPvclkDevice);
	    adKeywordViewConditionMap.put("pageNo",String.valueOf(pageNo));
	    adKeywordViewConditionMap.put("pageSize",String.valueOf(pageSize));
	    adKeywordViewConditionMap.put("keyword",keyword);
	    adKeywordViewConditionMap.put("searchAdStatus",searchAdStatus);
	    adKeywordViewConditionMap.put("adGroupSeq",adGroupSeq);
	    log.info("---------------startTime=" + new Date());
	    adKeywordViewVO = pfpAdKeywordService.getAdKeywordViewReport(adKeywordViewConditionMap);
	    totalVO = pfpAdKeywordService.getAdKeywordViewReportSize(adKeywordViewConditionMap);
	    totalSize = totalVO.getDataSize();
	    totalCount = totalVO.getDataSize();
	    
	    pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    log.info("---------------endTime=" + new Date());
		return SUCCESS;
	}
	
	public void setPfpAdKeywordService(IPfpAdKeywordService pfpAdKeywordService) {
		this.pfpAdKeywordService = pfpAdKeywordService;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setSearchAdStatus(String searchAdStatus) {
		this.searchAdStatus = searchAdStatus;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
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

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}

	public String getAdGroupSeq() {
		return adGroupSeq;
	}

	public String getAdKeywordPvclkDevice() {
		return adKeywordPvclkDevice;
	}

	public void setAdKeywordPvclkDevice(String adKeywordPvclkDevice) {
		this.adKeywordPvclkDevice = adKeywordPvclkDevice;
	}

	public List<PfpAdKeywordViewVO> getAdKeywordViewVO() {
		return adKeywordViewVO;
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

	public int getTotalCost() {
		return totalCost;
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

	public PfpAdKeywordViewVO getTotalVO() {
		return totalVO;
	}
	
}
