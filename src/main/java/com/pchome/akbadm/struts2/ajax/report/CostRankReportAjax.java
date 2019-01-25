package com.pchome.akbadm.struts2.ajax.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.service.ad.PfpAdPvclkService;
import com.pchome.akbadm.db.vo.AdPvclkVO;
import com.pchome.akbadm.report.pdf.APDFReport;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class CostRankReportAjax extends BaseCookieAction{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1572086139145889336L;
	private PfpAdPvclkService pfpAdPvclkService;
	private APDFReport costRankQueryReport;
	private String startDate;
	private String endDate;
	private String pageSize;

	
	private List<AdPvclkVO> adPvclkVOs;

	
	private String fileName;
	private InputStream pdfStream = null;
	
	
	public String searchCostRankReportAjax() throws Exception {

		//log.info(" pageSize  = "+pageSize);
		adPvclkVOs = pfpAdPvclkService.findAdPvclkCostRank(startDate, endDate, Integer.parseInt(pageSize));
		
		return SUCCESS;
	}
	
	public String downloadCostRankReportAjax() throws Exception {
			
		this.fileName = "CostRankReport_" + DateValueUtil.getInstance().dateToString(new Date()) + ".pdf";
		List<String> querys = new ArrayList<String>();
		querys.add(pageSize);
		querys.add(startDate);
		querys.add(endDate);

		this.pdfStream = new ByteArrayInputStream(costRankQueryReport.getPdfStream(querys));
		
		return SUCCESS;
	}


	public void setPfpAdPvclkService(PfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}
	

	public void setCostRankQueryReport(APDFReport costRankQueryReport) {
		this.costRankQueryReport = costRankQueryReport;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public List<AdPvclkVO> getAdPvclkVOs() {
		return adPvclkVOs;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getPdfStream() {
		return pdfStream;
	}
	
	
}
