package com.pchome.akbadm.struts2.action.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 行動裝置成效
 */
public class AdMobileOSReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdReportService adReportService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String adMobileOS; //行動裝置("all":全部, "Android":Android, "IOS":IOS, "":其他)
	private String customerInfoId; //客戶編號

	//明細
	private String reportDate;

	//查詢結果
	private List<AdActionReportVO> dataList = new ArrayList<AdActionReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";
	
	/**
	 * 查詢 PfpAdOsReport(行動裝置成效) 資料
	 * @return String 行動裝置成效列表
	 */
	public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adMobileOS = " + adMobileOS);
		log.info(">>> customerInfoId = " + customerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
		} else {

			this.dataList = adReportService.getAdMobileOSReport(startDate, endDate, adMobileOS, customerInfoId);

			if (dataList.size()==0) {
				this.message = "查無資料！";
			}
		}

		return SUCCESS;
	}

	/**
	 * 下載 PfpAdOsReport(行動裝置成效) 資料
	 * @return String 行動裝置成效列表
	 */
	public String downloadAdOs() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adMobileOS = " + adMobileOS);
		log.info(">>> customerInfoId = " + customerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("行動裝置成效_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = adReportService.getAdMobileOSReport(startDate, endDate, adMobileOS, customerInfoId);

	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4, 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdMobileOsReportPdf(doc, dataList,
					startDate, endDate, adMobileOS, customerInfoId);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}
	
	/**
	 * 查詢 PfpAdOsReport(行動裝置成效) 明細資料
	 * @return String 行動裝置成效明細列表
	 */
	public String adMobileOsReportDetail() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adMobileOS = " + adMobileOS);
		log.info(">>> customerInfoId = " + customerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
		} else {

			this.dataList = adReportService.getAdMobileOSReportDetail(startDate, endDate, adMobileOS, customerInfoId);

			if (dataList.size()==0) {
				this.message = "查無資料！";
			}
		}

		return SUCCESS;
	}

	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAdMobileOS() {
		return adMobileOS;
	}

	public void setAdMobileOS(String adMobileOS) {
		this.adMobileOS = adMobileOS;
	}

	public Map<String, String> getAdTypeSelectOptionsMap() {
		return ComponentUtils.getAdTypeSelectOptionsMap();
	}

	public Map<String, String> getAdStyleSelectOptionsMap() {
		return ComponentUtils.getAdStyleSelectOptionsMap();
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

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public List<AdActionReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdActionReportVO> dataList) {
		this.dataList = dataList;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public Map<String, String> getAdOsMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("all", "全部");
		map.put("IOS", "IOS");
		map.put("Android", "Android");
		map.put("Windows", "Windows");
		map.put("", "其他");
		return map;
	}
}
