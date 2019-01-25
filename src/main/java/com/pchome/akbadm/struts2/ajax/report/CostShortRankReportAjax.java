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

public class CostShortRankReportAjax extends BaseCookieAction{



	/**
	 * 
	 */
	private static final long serialVersionUID = -3310488868910236791L;
	
	private PfpAdPvclkService pfpAdPvclkService;
	private APDFReport costShortRankQueryReport;
	private String startDate;
	private String endDate;
	private String pageSize;

	
	private List<AdPvclkVO> adPvclkVOs;

	
	private String fileName;
	private InputStream pdfStream = null;
	
	
	public String searchCostShortRankReportAjax() throws Exception {

		//log.info(" pageSize  = "+pageSize);
		adPvclkVOs = pfpAdPvclkService.findAdPvclkCostShortRank(startDate, endDate, Integer.parseInt(pageSize));
		
		
		return SUCCESS;
	}
	
	public String downloadCostShortRankReportAjax() throws Exception {
			
		this.fileName = "CostShortRankReport_" + DateValueUtil.getInstance().dateToString(new Date()) + ".pdf";
		List<String> querys = new ArrayList<String>();
		querys.add(pageSize);
		querys.add(startDate);
		querys.add(endDate);

		this.pdfStream = new ByteArrayInputStream(costShortRankQueryReport.getPdfStream(querys));
		
		return SUCCESS;
	}


	public void setPfpAdPvclkService(PfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}


	public void setCostShortRankQueryReport(APDFReport costShortRankQueryReport) {
		this.costShortRankQueryReport = costShortRankQueryReport;
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
