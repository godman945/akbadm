package com.pchome.akbadm.struts2.action.pfbx.report;

import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusDetailReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;
import com.pchome.akbadm.struts2.BaseCookieAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class pfbInComeReportAction extends BaseCookieAction
{
	IAdmBonusDetailReportService admBonusDetailReportService;

	private String reportDate;
	private String sdate;
	private String edate;
	private String returnFlag;

	// 畫面VO
	private List<PfbxInComeReportVo> PfbxInComeReportVoList;

	public String execute()
	{
		return SUCCESS;
	}

	public String showInComeDetail() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date DreportDate = sdf.parse(reportDate);
		
		//取得VO
		PfbxInComeReportVoList = admBonusDetailReportService.getPfbxInComeReportVoList(DreportDate);

		log.info("...sDate=" + sdate);
		log.info("...eDate=" + edate);
		log.info("...reportDate=" + reportDate);
		
		return SUCCESS;
	}

	public void setAdmBonusDetailReportService(IAdmBonusDetailReportService admBonusDetailReportService)
	{
		this.admBonusDetailReportService = admBonusDetailReportService;
	}

	public void setReportDate(String reportDate)
	{
		this.reportDate = reportDate;
	}
	
	public String getReportDate()
	{
		return reportDate;
	}

	public List<PfbxInComeReportVo> getPfbxInComeReportVoList()
	{
		return PfbxInComeReportVoList;
	}

	public String getSdate()
	{
		return sdate;
	}

	public void setSdate(String sdate)
	{
		this.sdate = sdate;
	}

	public String getEdate()
	{
		return edate;
	}

	public void setEdate(String edate)
	{
		this.edate = edate;
	}

	public String getReturnFlag()
	{
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag)
	{
		this.returnFlag = returnFlag;
	}

	
}
