package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.service.template.ITemplateProductService;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

public class AdReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdReportService adReportService;
	private IPfpCustomerInfoService pfpCustomerInfoService;
	private IPfpAdService pfpAdService;
	private IPfdAccountService pfdAccountService;
	private ITemplateProductService templateProductService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String adType; //廣告形式("":全選, "1":找東西廣告, "2":PChome頻道廣告)
	private String sortMode; //排序方式("pv_sum": 依曝光數, "clk_sum": 依點擊率, "price_sum":依費用)
	private String displayCount = "50"; //顯示前幾名
	private String pfdCustomerInfoId; //PFD帳戶
	private String templateProductSeq; //樣板序號

	//明細參數
	private String keyword; //關鍵字

	//查詢結果
	private List<AdReportVO> dataList = new ArrayList<AdReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";
	
	//總計
	private long totalSize = 0;					//總比數
	private long totalPv = 0;					//曝光數	
	private long totalClk = 0;					//點擊數	
	private float totalClkRate = 0;				//點擊率
	private float totalClkPriceAvg = 0;			//平均點選出價
	private long totalPriceSum = 0;				//費用

	public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> templateProductSeq = " + templateProductSeq);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.dataList = adReportService.getAdReportList(startDate, endDate, adType,
					sortMode, Integer.parseInt(displayCount), pfdCustomerInfoId, templateProductSeq);

			if (dataList.size()==0) {

				this.message = "查無資料！";

			} else {
				totalSize = dataList.size();
				for (int i=0; i<dataList.size(); i++) {
					AdReportVO vo = dataList.get(i);

					PfpCustomerInfo customerInfo = pfpCustomerInfoService.get(vo.getCustomerId());
					if (customerInfo!=null) {
						vo.setCustomerName(customerInfo.getCustomerInfoTitle());
					}
					
					if(vo.getKwPvSum() != null){
	    				totalPv += Integer.parseInt(vo.getKwPvSum().replaceAll(",", ""));		
	    			}
	    			if(vo.getKwClkSum() != null){
	    				totalClk += Integer.parseInt(vo.getKwClkSum().replaceAll(",", "")); 				
	    			}
	    			if(vo.getKwPriceSum() != null){
	    				totalPriceSum += Integer.parseInt(vo.getKwPriceSum().replaceAll(",", "")); 				
	    			}
	    			
	    			if (totalClk==0 || totalPv==0) {
						totalClkRate = 0;
					} else {
						totalClkRate = (float) ((double) totalClk*100/totalPv);
					}
					
	    			if (totalClk==0 || totalPriceSum==0) {
	    				totalClkPriceAvg = 0;
					} else {
						totalClkPriceAvg = (float) ((double) totalPriceSum/totalClk);
					}
	    			
				}
			}
		}

		return SUCCESS;
	}

	public String downloadAd() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> sortMode = " + sortMode);
		log.info(">>> displayCount = " + displayCount);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> templateProductSeq = " + templateProductSeq);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("廣告成效報表_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = adReportService.getAdReportList(startDate, endDate, adType,
					sortMode, Integer.parseInt(displayCount), pfdCustomerInfoId, templateProductSeq);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4.rotate(), 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			String strAdType;
			Map<String, String> adTypeMap = this.getAdTypeSelectOptionsMap();
			if (adTypeMap.containsKey(adType)) {
				strAdType = adTypeMap.get(adType);
			} else {
				strAdType = "全部";
			}

			String strSortMode = this.getSortModeMap().get(sortMode);

			for (int i=0; i<dataList.size(); i++) {
				AdReportVO vo = dataList.get(i);

				PfpCustomerInfo customerInfo = pfpCustomerInfoService.get(vo.getCustomerId());
				if (customerInfo!=null) {
					vo.setCustomerName(customerInfo.getCustomerInfoTitle());
				}

				PfpAd pfpAd = pfpAdService.getPfpAdBySeq(vo.getAdSeq());

				String adTitle = "";

				if (pfpAd!=null) {
					Iterator<PfpAdDetail> it = pfpAd.getPfpAdDetails().iterator();
					while (it.hasNext()) {
						PfpAdDetail adDetail = it.next();
						if (adDetail.getAdDetailId().equals("title")) {
							adTitle = adDetail.getAdDetailContent();
						}
					}
				}

				vo.setAdTitle(adTitle);

			}

			pdfUtil.prepareAdReportPdf(doc, dataList,
					startDate, endDate, strAdType, strSortMode, displayCount);

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

	public void setAdReportService(IAdReportService adReportService) {
		this.adReportService = adReportService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
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

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getSortMode() {
		return sortMode;
	}

	public void setSortMode(String sortMode) {
		this.sortMode = sortMode;
	}

	public List<AdReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdReportVO> dataList) {
		this.dataList = dataList;
	}

	public Map<String, String> getAdTypeSelectOptionsMap() {
		return ComponentUtils.getAdTypeSelectOptionsMap();
	}

	public Map<String, String> getSortModeMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("clk_sum", "點擊次數");
		map.put("pv_sum", "曝光數");
		map.put("clk_rate", "點選率");
		map.put("clk_price_avg", "平均點選出價");
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

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return pfdCustomerInfos;
	}

	public List<AdmTemplateProduct> getTemplateProductList() {

		List<AdmTemplateProduct> templateProductList = new ArrayList<AdmTemplateProduct>();

		try {

			templateProductList = templateProductService.getTemplateProductByCondition(null, null, null, null, null);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return templateProductList;
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

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
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

	public void setPfpAdService(IPfpAdService pfpAdService) {
		this.pfpAdService = pfpAdService;
	}

	public void setTemplateProductService(ITemplateProductService templateProductService) {
		this.templateProductService = templateProductService;
	}

	public String getTemplateProductSeq() {
		return templateProductSeq;
	}

	public void setTemplateProductSeq(String templateProductSeq) {
		this.templateProductSeq = templateProductSeq;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public long getTotalPv() {
		return totalPv;
	}

	public long getTotalClk() {
		return totalClk;
	}

	public float getTotalClkRate() {
		return totalClkRate;
	}

	public float getTotalClkPriceAvg() {
		return totalClkPriceAvg;
	}

	public long getTotalPriceSum() {
		return totalPriceSum;
	}
}
