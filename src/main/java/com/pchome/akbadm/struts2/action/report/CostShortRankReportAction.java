package com.pchome.akbadm.struts2.action.report;

import java.util.Date;

import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class CostShortRankReportAction extends BaseCookieAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4483310207258407425L;
	
	private String startDate;
	private String endDate;
	
	public String execute() throws Exception{
		
		endDate = DateValueUtil.getInstance().dateToString(new Date());
		startDate = DateValueUtil.getInstance().getDateValue(-7, DateValueUtil.DBPATH);
		
		return SUCCESS;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
}
