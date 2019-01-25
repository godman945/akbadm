package com.pchome.akbadm.struts2.ajax.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.customerInfo.PfpCustomerInfoService;
import com.pchome.akbadm.db.vo.CustomerInfoReportVO;
import com.pchome.akbadm.report.pdf.APDFReport;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class CustomerInfoReportAjax extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private PfpCustomerInfoService pfpCustomerInfoService;

	private APDFReport customerInfoQueryReport;

	//查詢參數
	private String startDate;
	private String endDate;
	private String pfdCustomerInfoId;
	private String orderType; //儲值類型( 儲值金、廣告金、回饋金... )
	private String payType; //付款方式

	private List<CustomerInfoReportVO> customerInfoReportVOs;
	
	private String fileName;
	private InputStream pdfStream = null;

	private String message = "";
	
	public String searchCustomerInfoReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> orderType = " + orderType);
		log.info(">>> payType = " + payType);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			customerInfoReportVOs = pfpCustomerInfoService.findPfpCustomerInfosByDate(startDate, endDate,
					pfdCustomerInfoId, orderType, payType);

			if (customerInfoReportVOs==null || customerInfoReportVOs.size()==0) {
				this.message = "查無資料！";
			}
		}

		return SUCCESS;
	}
	
	public String downloadCustomerInfoReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> orderType = " + orderType);
		log.info(">>> payType = " + payType);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		String reportNo = dateFormat.format(new Date());

		this.fileName =  URLEncoder.encode("帳戶新開通數_" + reportNo + ".pdf", "UTF-8");

		List<String> querys = new ArrayList<String>();
		querys.add(startDate);
		querys.add(endDate);
		querys.add(pfdCustomerInfoId);
		querys.add(orderType);
		querys.add(payType);

		this.pdfStream = new ByteArrayInputStream(customerInfoQueryReport.getPdfStream(querys));
		
		return SUCCESS;
	}

	public void setPfpCustomerInfoService(PfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public void setCustomerInfoQueryReport(APDFReport customerInfoQueryReport) {
		this.customerInfoQueryReport = customerInfoQueryReport;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<CustomerInfoReportVO> getCustomerInfoReportVOs() {
		return customerInfoReportVOs;
	}
	
	public String getFileName() {
		return fileName;
	}

	public InputStream getPdfStream() {
		return pdfStream;
	}

	public String getMessage() {
		return message;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}
