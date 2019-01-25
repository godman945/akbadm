package com.pchome.akbadm.struts2.action.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdKeywordReportService;
import com.pchome.akbadm.db.vo.KeywordReportVO;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 關鍵字成效報表 
 */
public class AdKeywordReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdKeywordReportService keywordReportService;
	private IPfpCustomerInfoService pfpCustomerInfoService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String keywordType; //廣告形式("":全選, "1":找東西廣告, "2":PChome頻道廣告)
	private String sortMode; //排序方式("pv_sum": 依曝光數, "clk_sum": 依點擊率, "price_sum":依費用)
	private String displayCount = "50"; //顯示前幾名
	private String pfdCustomerInfoId; //PFD帳戶

	//明細參數
	private String keyword; //關鍵字

	//查詢結果
	private List<KeywordReportVO> dataList = new ArrayList<KeywordReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";

	public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> keywordType = " + keywordType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.dataList = keywordReportService.getAdKeywordReportList(startDate, endDate,
					keywordType, sortMode, Integer.parseInt(displayCount), pfdCustomerInfoId);

			if (dataList.size()==0) {
				this.message = "查無資料！";
			}

		}
		return SUCCESS;
	}

	public String detail() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> keywordType = " + keywordType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> keyword = " + keyword);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		this.keyword = URLDecoder.decode(this.keyword, "utf-8");
		this.keyword = new String(keyword.getBytes("utf-8"));

		this.dataList = keywordReportService.getAdKeywordReportDetail(startDate, endDate,
				keywordType, sortMode, Integer.parseInt(displayCount), keyword, pfdCustomerInfoId);

		for (int i=0; i<dataList.size(); i++) {
			KeywordReportVO vo = dataList.get(i);
			PfpCustomerInfo customerInfo = pfpCustomerInfoService.get(vo.getCustomerId());
			if (customerInfo!=null) {
				vo.setCustomerName(customerInfo.getCustomerInfoTitle());
			}
		}

		return SUCCESS;
	}

	public String downloadKeyword() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> keywordType = " + keywordType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("關鍵字成效報表_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = keywordReportService.getAdKeywordReportList(startDate, endDate,
					keywordType, sortMode, Integer.parseInt(displayCount), pfdCustomerInfoId);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

			//Document doc = new Document(PageSize.A4.rotate(), 0, 0, 40, 20);
	        Document doc = new Document(PageSize.A4, 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			String strKeywordType;
			Map<String, String> keywordTypeMap = this.getKeywordTypeSelectOptionsMap();
			if (keywordTypeMap.containsKey(keywordType)) {
				strKeywordType = keywordTypeMap.get(keywordType);
			} else {
				strKeywordType = "全部";
			}

			String strSortMode = this.getSortModeMap().get(sortMode);

			pdfUtil.prepareKeywordReportPdf(doc, dataList,
					startDate, endDate, strKeywordType, strSortMode, displayCount);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}

	public String downloadKeywordDetail() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> keywordType = " + keywordType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> keyword = " + keyword);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("關鍵字明細成效報表_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = keywordReportService.getAdKeywordReportDetail(startDate, endDate,
					keywordType, sortMode, Integer.parseInt(displayCount), keyword, pfdCustomerInfoId);
			log.info(">>> dataList.size() = " + dataList.size());

			if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

			for (int i=0; i<dataList.size(); i++) {
				KeywordReportVO vo = dataList.get(i);
				PfpCustomerInfo customerInfo = pfpCustomerInfoService.get(vo.getCustomerId());
				if (customerInfo!=null) {
					vo.setCustomerName(customerInfo.getCustomerInfoTitle());
				}
			}

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

			Document doc = new Document(PageSize.A4.rotate(), 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			String strKeywordType;
			Map<String, String> keywordTypeMap = this.getKeywordTypeSelectOptionsMap();
			if (keywordTypeMap.containsKey(keywordType)) {
				strKeywordType = keywordTypeMap.get(keywordType);
			} else {
				strKeywordType = "全部";
			}

			String strSortMode = this.getSortModeMap().get(sortMode);

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareKeywordDetailPdf(doc, dataList,
					startDate, endDate, strKeywordType, strSortMode, displayCount);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
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

	public void setKeywordReportService(IAdKeywordReportService keywordReportService) {
		this.keywordReportService = keywordReportService;
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

	public String getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
	}

	public String getSortMode() {
		return sortMode;
	}

	public void setSortMode(String sortMode) {
		this.sortMode = sortMode;
	}

	public List<KeywordReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<KeywordReportVO> dataList) {
		this.dataList = dataList;
	}

	public Map<String, String> getKeywordTypeSelectOptionsMap() {
		return ComponentUtils.getKeywordTypeSelectOptionsMap();
	}

	public Map<String, String> getSortModeMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("clk_sum", "點擊次數");
		map.put("pv_sum", "曝光數");
		map.put("price_sum", "費用");
		return map;
	}

	public Map<String, String> getDisplayCountMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("50", "50");
		map.put("100", "100");
		map.put("500", "500");
		map.put("1000", "1000");
		return map;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(String displayCount) {
		this.displayCount = displayCount;
	}

	public void setPfpCustomerInfoService(
			IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
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

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
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
}
