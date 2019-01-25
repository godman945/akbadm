package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdKeywordReportService;
import com.pchome.akbadm.db.vo.KeywordReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 關鍵字出價報表 
 */
public class AdKeywordOfferPriceReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdKeywordReportService keywordReportService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String keywordType; //廣告形式("":全選, "1":找東西廣告, "2":PChome頻道廣告)
	private String displayCount = "50"; //顯示前幾名
	private String searchText; //搜尋字
	private String pfdCustomerInfoId; //PFD帳戶

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
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> searchText = " + searchText);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.dataList = keywordReportService.getAdKeywordOfferPriceReportList(startDate, endDate,
					keywordType, searchText, Integer.parseInt(displayCount), pfdCustomerInfoId);

			if (dataList.size()==0) {
				this.message = "查無資料！";
			}

		}
		return SUCCESS;
	}

	public String downloadKeywordOfferPrice() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> keywordType = " + keywordType);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> searchText = " + searchText);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("關鍵字出價報表_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = keywordReportService.getAdKeywordOfferPriceReportList(startDate, endDate,
					keywordType, searchText, Integer.parseInt(displayCount), pfdCustomerInfoId);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

			//Document doc = new Document(PageSize.A4.rotate(), 0, 0, 40, 20);
	        Document doc = new Document(PageSize.A4.rotate(), 0, 0, 20, 20);

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

			pdfUtil.prepareKeywordOfferPriceReportPdf(doc, dataList,
					startDate, endDate, strKeywordType, searchText, displayCount);

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

	public String getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(String displayCount) {
		this.displayCount = displayCount;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<KeywordReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<KeywordReportVO> dataList) {
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

	public void setKeywordReportService(IAdKeywordReportService keywordReportService) {
		this.keywordReportService = keywordReportService;
	}

	public Map<String, String> getKeywordTypeSelectOptionsMap() {
		return ComponentUtils.getKeywordTypeSelectOptionsMap();
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

	public Map<String, String> getDisplayCountMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("50", "50");
		map.put("100", "100");
		map.put("500", "500");
		map.put("1000", "1000");
		return map;
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
