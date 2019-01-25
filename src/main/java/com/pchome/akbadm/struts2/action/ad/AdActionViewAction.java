package com.pchome.akbadm.struts2.action.ad;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdType;
import com.pchome.soft.util.DateValueUtil;

public class AdActionViewAction extends BaseCookieAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String,String> dateSelectMap = DateValueUtil.getInstance().getDateRangeMap(); // 查詢日期頁面顯示
	private String startDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);					// 開始日期
	private String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);	// 結束日期

	private EnumAdType[] searchAdType;
	
	public String execute() throws Exception{
		
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
}
