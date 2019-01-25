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
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.trans.PfpTransDetailService;
import com.pchome.akbadm.db.vo.PfpTransDetailReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

/**
 * 帳戶總使用明細
 */
public class TotalTransReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private PfpTransDetailService pfpTransDetailService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String pfdCustomerInfoId; //PFD帳戶

	//明細
	private String reportDate;

	//加總
	private double add_sum; //加值
	private double tax_sum; //稅
	private double spend_sum; //花費
	private double invalid_sum; //惡意點擊
	private double refund_sum; //惡意點擊
	private double free_sum; //免費贈送

	//查詢結果
	private List<PfpTransDetailReportVO> dataList = new ArrayList<PfpTransDetailReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";

	@Override
    public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			this.dataList = pfpTransDetailService.findPfpTransDetailReport(startDate, endDate,
					pfdCustomerInfoId);

			if ((dataList == null) || (dataList.size() == 0)) {

				this.message = "查無資料！";

			} else {

				//整理加總
				for (int i=0; i<dataList.size(); i++) {
					PfpTransDetailReportVO vo = dataList.get(i);
					add_sum += vo.getAdd();
					tax_sum += vo.getTax();
					spend_sum += vo.getSpend();
					invalid_sum += vo.getInvalid();
					refund_sum += vo.getRefund();
					free_sum += vo.getFree();
				}
			}
		}

		return SUCCESS;
	}

	public String downloadTotalTrans() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("總使用明細_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = pfpTransDetailService.findPfpTransDetailReport(startDate, endDate,
					pfdCustomerInfoId);

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

			pdfUtil.prepareTotalTransReportPdf(doc, dataList, startDate, endDate);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}

	public String totalTransReportDetail() throws Exception {

		log.info(">>> reportDate = " + reportDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		this.dataList = pfpTransDetailService.findPfpTransDetailReportDetail(reportDate, pfdCustomerInfoId);

		if (dataList.size()==0) {

			this.message = "查無資料！";
			return INPUT;

		} else {

			//整理加總
			for (int i=0; i<dataList.size(); i++) {
				PfpTransDetailReportVO vo = dataList.get(i);
				add_sum += vo.getAdd();
				tax_sum += vo.getTax();
				spend_sum += vo.getSpend();
				invalid_sum += vo.getInvalid();
				refund_sum += vo.getRefund();
				free_sum += vo.getFree();
			}
		}

		return SUCCESS;
	}

	public String downloadTotalTransDetail() throws Exception {

		log.info(">>> reportDate = " + reportDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("單日使用明細_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = pfpTransDetailService.findPfpTransDetailReportDetail(reportDate, pfdCustomerInfoId);

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

			pdfUtil.prepareTotalTransDetailPdf(doc, dataList, reportDate);

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

	public void setPfpTransDetailService(PfpTransDetailService pfpTransDetailService) {
		this.pfpTransDetailService = pfpTransDetailService;
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

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public double getAdd_sum() {
		return add_sum;
	}

	public void setAdd_sum(double addSum) {
		add_sum = addSum;
	}

	public double getTax_sum() {
		return tax_sum;
	}

	public void setTax_sum(double taxSum) {
		tax_sum = taxSum;
	}

	public double getSpend_sum() {
		return spend_sum;
	}

	public void setSpend_sum(double spendSum) {
		spend_sum = spendSum;
	}

	public double getInvalid_sum() {
		return invalid_sum;
	}

	public void setInvalid_sum(double invalidSum) {
		invalid_sum = invalidSum;
	}

	public double getRefund_sum() {
		return refund_sum;
	}

	public void setRefund_sum(double refundSum) {
		refund_sum = refundSum;
	}

	public double getFree_sum() {
		return free_sum;
	}

	public void setFree_sum(double freeSum) {
		free_sum = freeSum;
	}

	public List<PfpTransDetailReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PfpTransDetailReportVO> dataList) {
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
