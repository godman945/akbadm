package com.pchome.akbadm.struts2.action.report;

import java.util.List;

import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.vo.report.CheckBillReportVo;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class CheckBillReportAction extends BaseCookieAction{

	private IPfpAdPvclkService pfpAdPvclkService;
	
	private String date;
	private List<CheckBillReportVo> vos;
	
	public String execute() {
		
		log.info(" date: "+date);
		
		vos = pfpAdPvclkService.findCheckBillReportToVo(date);
		
		return SUCCESS;
	}

	public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<CheckBillReportVo> getVos() {
		return vos;
	}
	
	
	
	
}
