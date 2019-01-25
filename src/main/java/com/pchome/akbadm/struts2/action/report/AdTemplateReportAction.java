package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdTemplateReportService;
import com.pchome.akbadm.db.service.template.ITemplateProductService;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 廣告樣版成效表 
 */
public class AdTemplateReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfdAdTemplateReportService pfdAdTemplateReportService;
	private ITemplateProductService templateProductService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String pfdCustomerInfoId; // 經銷歸屬

	//查詢結果
	private List<AdTemplateReportVO> dataList = new ArrayList<AdTemplateReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";

	public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.dataList = pfdAdTemplateReportService.findPfdAdTemplateReport(startDate, endDate, pfdCustomerInfoId);
			log.info(">>> dataList.size() = " + dataList.size());

			if (dataList.size()==0) {

				this.message = "查無資料！";

			} else {

				List<AdmTemplateProduct> admTemplateProductList = templateProductService.getTemplateProductByCondition(null, null, null, null, null);

				Map<String, String> admTemplateProductMap = new HashMap<String, String>();
				for (int i=0; i<admTemplateProductList.size(); i++) {
					AdmTemplateProduct admTemplateProduct = admTemplateProductList.get(i);
					admTemplateProductMap.put(admTemplateProduct.getTemplateProductSeq(), admTemplateProduct.getTemplateProductName());
				}

				for (int i=0; i<dataList.size(); i++) {
					AdTemplateReportVO vo = dataList.get(i);
					vo.setTemplateProdName(admTemplateProductMap.get(vo.getTemplateProdSeq()));
				}
			}

		}

		log.info(">>> go");

		return SUCCESS;
	}

	public String downloadAdTemplate() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("廣告成效報表_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = pfdAdTemplateReportService.findPfdAdTemplateReport(startDate, endDate, pfdCustomerInfoId);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

			List<AdmTemplateProduct> admTemplateProductList = templateProductService.getTemplateProductByCondition(null, null, null, null, null);

			Map<String, String> admTemplateProductMap = new HashMap<String, String>();
			for (int i=0; i<admTemplateProductList.size(); i++) {
				AdmTemplateProduct admTemplateProduct = admTemplateProductList.get(i);
				admTemplateProductMap.put(admTemplateProduct.getTemplateProductSeq(), admTemplateProduct.getTemplateProductName());
			}

			for (int i=0; i<dataList.size(); i++) {
				AdTemplateReportVO vo = dataList.get(i);
				vo.setTemplateProdName(admTemplateProductMap.get(vo.getTemplateProdSeq()));
			}

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4, 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdTemplateReportPdf(doc, dataList, startDate, endDate);

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

	public List<AdTemplateReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdTemplateReportVO> dataList) {
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

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public void setPfdAdTemplateReportService(IPfdAdTemplateReportService pfdAdTemplateReportService) {
		this.pfdAdTemplateReportService = pfdAdTemplateReportService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public void setTemplateProductService(ITemplateProductService templateProductService) {
		this.templateProductService = templateProductService;
	}
}
