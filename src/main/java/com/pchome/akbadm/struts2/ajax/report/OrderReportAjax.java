package com.pchome.akbadm.struts2.ajax.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.vo.AdmRecognizeRecordVO;
import com.pchome.akbadm.report.pdf.APDFReport;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class OrderReportAjax extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IAdmRecognizeRecordService recognizeRecordService;
	private APDFReport orderQueryReport;

	private String startDate;
	private String endDate;
	private String pfdCustomerInfoId;
	private String payType;

	private List<AdmRecognizeRecordVO> recognizeRecords;
	
	private String fileName;
	private InputStream pdfStream = null;
	
	private String message = "";

	public String searchOrderReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.recognizeRecords = recognizeRecordService.findRecognizeRecords(startDate, endDate,
					pfdCustomerInfoId, payType);
			log.info(">>> recognizeRecords.size() = " + recognizeRecords.size());

			if (recognizeRecords==null || recognizeRecords.size()==0) {
				this.message = "查無資料！";
			}
		}

		return SUCCESS;
	}
	
	public String downloadOrderReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		String reportNo = dateFormat.format(new Date());

		this.fileName =  URLEncoder.encode("帳戶每日儲值_" + reportNo + ".pdf", "UTF-8");

		List<String> querys = new ArrayList<String>();
		querys.add(startDate);
		querys.add(endDate);
		querys.add(pfdCustomerInfoId);
		querys.add(payType);

		this.pdfStream = new ByteArrayInputStream(orderQueryReport.getPdfStream(querys));
		
		return SUCCESS;
	}

	public void setRecognizeRecordService(IAdmRecognizeRecordService recognizeRecordService) {
		this.recognizeRecordService = recognizeRecordService;
	}

	public void setOrderQueryReport(APDFReport orderQueryReport) {
		this.orderQueryReport = orderQueryReport;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public List<AdmRecognizeRecordVO> getRecognizeRecords() {
		return recognizeRecords;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}
