 package com.pchome.akbadm.struts2.ajax.pfbx.report;

import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusBillReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;
import com.pchome.akbadm.struts2.BaseCookieAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class pfbInComeReportAjax extends BaseCookieAction
{
	
	private static final long serialVersionUID = 1L;
	private String sdate;
	private String edate;
	private String reportDate;
	
	//畫面VO
	private List<PfbxInComeReportVo> PfbxInComeReportVo;
	private List<Object[]> pfpPriceUsedDetail;
	
	//Service
	private IAdmBonusBillReportService admBonusBillReportService;
	private IPfpAdPvclkService pfpAdPvclkService;
	private IPfdBonusDayReportService pfdBonusDayReportService;
	
	public String execute() throws Exception
	{
		log.info("...in pfbInComeReportAjax");
		PfbxInComeReportVo = admBonusBillReportService.getPfbxInComeReportVoList5(sdate, edate);
		
		return SUCCESS;
	}
	
	public String pfbInComePFPDetailAjax() throws Exception
	{ 
		log.info("...reportDate=" + reportDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dreportDate = sdf.parse(reportDate);
		
		pfpPriceUsedDetail = pfpAdPvclkService.getListObjPFPDetailByReportDate(dreportDate);
		
		return SUCCESS;
	}
	
	public String pfbInComePFBDetailAjax() throws Exception
	{
		log.info("...reportDate=" + reportDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dreportDate = sdf.parse(reportDate);
		
		pfpPriceUsedDetail = pfpAdPvclkService.getListObjPFBDetailByReportDate(dreportDate);
		
		return SUCCESS;
	}
	
	public String pfbInComePFDDetailAjax() throws Exception
	{
		log.info("...reportDate=" + reportDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dreportDate = sdf.parse(reportDate);
		
		pfpPriceUsedDetail = pfdBonusDayReportService.getListObjPFDDetailByReportDate(dreportDate);
		
		return SUCCESS;
	}
	
	public void setSdate(String sdate)
	{
		this.sdate = sdate;
	}
	public void setEdate(String edate)
	{
		this.edate = edate;
	}
	public List<PfbxInComeReportVo> getPfbxInComeReportVo()
	{
		return PfbxInComeReportVo;
	}
	public void setAdmBonusBillReportService(IAdmBonusBillReportService admBonusBillReportService)
	{
		this.admBonusBillReportService = admBonusBillReportService;
	}
	public void setReportDate(String reportDate)
	{
		this.reportDate = reportDate;
	}
	public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService)
	{
		this.pfpAdPvclkService = pfpAdPvclkService;
	}
	public void setPfdBonusDayReportService(IPfdBonusDayReportService pfdBonusDayReportService)
	{
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}

	public List<Object[]> getPfpPriceUsedDetail()
	{
		return pfpPriceUsedDetail;
	}
	
	

}
