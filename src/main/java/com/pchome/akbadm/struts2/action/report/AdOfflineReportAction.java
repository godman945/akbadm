package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 廣告十日內即將下檔報表 
 */
public class AdOfflineReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdReportService adReportService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String pfdCustomerInfoId; // 經銷歸屬

	//查詢結果
	private List<AdActionReportVO> dataList = new ArrayList<AdActionReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";

	public String execute() throws Exception {

		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		String startDate = dFormat.format(today);

		//10天
		cal.add(Calendar.DATE, 10);
		Date after_ten_day = cal.getTime();
		String endDate = dFormat.format(after_ten_day);
		
		log.info(">>> startDate = " + startDate + ", endDate = " + endDate);
		this.dataList = adReportService.getOfflineAdActionReportList(startDate, endDate, pfdCustomerInfoId);

		if (dataList.size()==0) {
			this.message = "查無資料！";
		}

		return SUCCESS;
	}

	public String downloadAdOffline() throws Exception {

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("廣告十日內即將下檔報表_" + reportNo + ".pdf", "UTF-8");

		try {
			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			String startDate = dFormat.format(today);

			//10天
			cal.add(Calendar.DATE, 10);
			Date after_ten_day = cal.getTime();
			String endDate = dFormat.format(after_ten_day);

			log.info(">>> startDate = " + startDate + ", endDate = " + endDate);
			this.dataList = adReportService.getOfflineAdActionReportList(startDate, endDate, pfdCustomerInfoId);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4.rotate(), -40, -40, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdOfflineReportPdf(doc, dataList);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}

	public List<PfdCustomerInfo> getCompanyList() {

		List<PfdCustomerInfo> pfdCustomerInfos = new ArrayList<PfdCustomerInfo>();

		try {

			String status = "'" + EnumPfdAccountStatus.START.getStatus() +
					"','" + EnumPfdAccountStatus.CLOSE.getStatus() +
					"','" + EnumPfdAccountStatus.STOP.getStatus() + "'";

			log.info(">>> status = " + status);

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("status", status);

			pfdCustomerInfos = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap);

		} catch(Exception ex) {
			log.info("Exception :" + ex);
		}

		return pfdCustomerInfos;
	}

	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public List<AdActionReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdActionReportVO> dataList) {
		this.dataList = dataList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getPdfStream() {
		return pdfStream;
	}

	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setAdReportService(IAdReportService adReportService) {
		this.adReportService = adReportService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}
}
