package com.pchome.akbadm.struts2.action.ad;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdType;
import com.pchome.soft.util.DateValueUtil;

public class AdGroupViewAction extends BaseCookieAction{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String,String> dateSelectMap = DateValueUtil.getInstance().getDateRangeMap(); 			// 查詢日期頁面顯示
	private String startDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);					// 開始日期
	private String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);	// 結束日期

	private String adStartDate;			// 傳入開始日期
	private String adEndDate;			// 傳入結束日期

	private String adActionSeq;
	private String dateType;
	private String userAccount;
	private String searchAdStatus;
	private String searchType;
	private String adclkDevice;
	private EnumAdType[] searchAdType;
	
	public String execute() throws Exception{
		if(StringUtils.isBlank(searchAdStatus))		searchAdStatus = "";
		if(StringUtils.isBlank(searchType))			searchType = "";
		if(StringUtils.isNotBlank(adStartDate)) {
			startDate = adStartDate;
		}
		if(StringUtils.isNotBlank(adEndDate)) {
			endDate = adEndDate;
		}

		searchAdType = EnumAdType.values();
		
		return SUCCESS;
	}

	public LinkedHashMap<String, String> getDateSelectMap() {
		return dateSelectMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getAdActionSeq() {
		return adActionSeq;
	}

	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}

	public String getAdStartDate() {
		return adStartDate;
	}

	public void setAdStartDate(String adStartDate) {
		this.adStartDate = adStartDate;
	}

	public String getAdEndDate() {
		return adEndDate;
	}

	public void setAdEndDate(String adEndDate) {
		this.adEndDate = adEndDate;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getSearchAdStatus() {
		return searchAdStatus;
	}

	public void setSearchAdStatus(String searchAdStatus) {
		this.searchAdStatus = searchAdStatus;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public EnumAdType[] getSearchAdType() {
		return searchAdType;
	}

	public Map<String, String> getAdPvclkDeviceMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "裝置");
		map.put("PC", "PC");
		map.put("mobile", "mobile");
		return map;
	}

	public String getAdclkDevice() {
	    return adclkDevice;
	}

	public void setAdclkDevice(String adclkDevice) {
	    this.adclkDevice = adclkDevice;
	}
	
}
