package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.recognize.AdmRecognizeRecordService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.vo.AdActionDalilyReportVO;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

/**
 * 廣告總成效表
 */
public class AdActionReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdReportService adReportService;
	private IAdmTransLossService admTransLossService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String adType; //廣告形式("":全選, 1:找東西廣告, 2:PChome頻道廣告)
	private String customerInfoId; //客戶編號
	private String pfdCustomerInfoId; // 經銷歸屬
	private String payType; //付款方式

	 //下載報表需要的查詢條件，從畫面傳到後端不用重撈
	private String adTypeText;
	private String pfdCustomerInfoIdText;
	private String payTypeText;

	//明細
	private String reportDate;

	//查詢結果
	private List<AdActionReportVO> dataList = new ArrayList<AdActionReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream pdfStream = null;

	//訊息
	private String message = "";


	//dalily report 報表測試
	 private InputStream downloadFileStream;
	 private String downloadFileName;
	 private AdmRecognizeRecordService admRecognizeRecordService;


	public void getDalilyReportData() throws Exception{

	    dailyReportDownload();

	}


	public String dailyReportDownload() throws Exception{
	    StringBuffer content = new StringBuffer();
	    String filename = "ALEX.xls";
	    content.append("報表名稱\t經銷商總廣告成效測試");
	    downloadFileName = URLEncoder.encode(filename, "UTF-8");
//	    downloadFileStream = new ByteArrayInputStream(("\uFEFF" + content.toString()).getBytes("UTF-16LE"));
	    List<AdActionDalilyReportVO> adActionDalilyReportVOList = new ArrayList<AdActionDalilyReportVO>();


	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    WritableWorkbook book = Workbook.createWorkbook(out);
	    WritableSheet sheet = book.createSheet("訂單攤提", 0);

	    Label customerInfoId = new Label(0, 1, "客戶ID");
	    Label recognizeOrderId = new Label(1, 1, "訂單編號");
	    Label customerInfoTitle = new Label(2, 1, "帳戶名稱");
	    Label orderType = new Label(3, 1, "訂單類型");
	    Label createDate = new Label(4, 1, "建立時間");
	    Label totalAddMoney = new Label(5, 1, "儲值金額");
	    Label costDate = new Label(6, 1, "攤提日期");
	    Label costPrice = new Label(7, 1, "花費");
	    Label saveDate = new Label(8, 1, "儲值日");
	    Label orderPrice = new Label(9, 1, "儲值金額");
	    Label lagTime = new Label(10, 1, "花費間隔日");
	    Label minusMoney = new Label(11, 1, "扣除後餘額");
	    Label remain = new Label(12, 1, "帳戶目前餘額");
	    sheet.addCell(customerInfoId);
	    sheet.addCell(recognizeOrderId);
	    sheet.addCell(customerInfoTitle);
	    sheet.addCell(orderType);
	    sheet.addCell(createDate);
	    sheet.addCell(totalAddMoney);
	    sheet.addCell(costDate);
	    sheet.addCell(costPrice);
	    sheet.addCell(saveDate);
	    sheet.addCell(orderPrice);
	    sheet.addCell(lagTime);
	    sheet.addCell(minusMoney);
	    sheet.addCell(remain);

	    int i = 1;
	    adActionDalilyReportVOList = admRecognizeRecordService.getAdAdmRecognizeDalilyReport();
	    for (AdActionDalilyReportVO adActionDalilyReportVO : adActionDalilyReportVOList) {
		i = i+1;
		Label customerInfoIdData = new Label(0, i, adActionDalilyReportVO.getCustomerInfoId());
		Label recognizeOrderIdData = new Label(1, i, adActionDalilyReportVO.getRecognizeOrderId());
		Label customerInfoTitleData = new Label(2, i, adActionDalilyReportVO.getCustomerInfoTitle());
		Label orderTypeData = new Label(3, i, adActionDalilyReportVO.getOrderType());
		Label createDateData = new Label(4, i, adActionDalilyReportVO.getCreateDate());
		Label totalAddMoneyData = new Label(5, i, adActionDalilyReportVO.getTotalAddMoney());
		Label costDateData = new Label(6, i, adActionDalilyReportVO.getCostDate());
		Label costPriceData = new Label(7, i, adActionDalilyReportVO.getCostPrice());
		Label saveDateData = new Label(8, i, adActionDalilyReportVO.getSaveDate());
		Label orderPriceData = new Label(9, i, adActionDalilyReportVO.getOrderPrice());
		Label lagTimeData = new Label(10, i, adActionDalilyReportVO.getLagTime());

		Label minusMoneyData = new Label(11, i, adActionDalilyReportVO.getMinusMoney());

		Label remainData = new Label(12, i, adActionDalilyReportVO.getRemain());


		sheet.addCell(customerInfoIdData);
		sheet.addCell(recognizeOrderIdData);
		sheet.addCell(customerInfoTitleData);
		sheet.addCell(orderTypeData);
		sheet.addCell(createDateData);
		sheet.addCell(totalAddMoneyData);
		sheet.addCell(costDateData);
		sheet.addCell(costPriceData);
		sheet.addCell(saveDateData);
		sheet.addCell(orderPriceData);
		sheet.addCell(lagTimeData);
		sheet.addCell(minusMoneyData);
		sheet.addCell(remainData);

	    }

	    book.write();
	    book.close();
	    downloadFileStream =  new ByteArrayInputStream(out.toByteArray());
	    return "SUCCESS";
	}











	/**
	 * 查詢 AdActionReport(總廣告成效) 資料
	 * @return String 總廣告成效列表
	 */
	@Override
    public String execute() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> customerInfoId = " + customerInfoId);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
		} else {

			// 舊版，讀取 pfp_ad_pvclk
			//this.dataList = adReportService.getAdActionReportList(startDate, endDate, adStyle, customerInfoId, adType);
			// 新版，讀取 pfd_ad_action_report，忽略廣告樣式，但是加入經銷商代碼
			this.dataList = adReportService.getAdActionReportList(startDate, endDate,
					customerInfoId, adType, pfdCustomerInfoId, payType);

			if (dataList.size()==0) {
				this.message = "查無資料！";
			}

			//超播金額
			DecimalFormat df2 = new DecimalFormat("###,###,###,###");
			for (int i=0; i<dataList.size(); i++) {
				AdActionReportVO vo = dataList.get(i);
				float overSpend = admTransLossService.selectTransLossSumByTransDate(vo.getReportDate());
				vo.setOverPriceSum(df2.format(overSpend));
			}
		}

		return SUCCESS;
	}

	/**
	 * 下載 AdActionReport(總廣告成效) 資料
	 * @return String 總廣告成效列表
	 */
	@Deprecated
	public String downloadAdAction() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> customerInfoId = " + customerInfoId);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);
		log.info(">>> pfdCustomerInfoIdText = " + pfdCustomerInfoIdText);
		log.info(">>> payTypeText = " + payTypeText);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		String reportNo = dateFormat.format(new Date());

		this.fileName =  URLEncoder.encode("總廣告成效_" + reportNo + ".pdf", "UTF-8");

		try {

			// 舊版，讀取 pfp_ad_pvclk
			//this.dataList = adReportService.getAdActionReportList(startDate, endDate, adStyle, customerInfoId, adType);
			// 新版，讀取 pfd_ad_action_report，忽略廣告樣式，但是加入經銷商代碼
			this.dataList = adReportService.getAdActionReportList(startDate, endDate,
					customerInfoId, adType, pfdCustomerInfoId, payType);

	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

			//超播金額
			DecimalFormat df2 = new DecimalFormat("###,###,###,###");
			for (int i=0; i<dataList.size(); i++) {
				AdActionReportVO vo = dataList.get(i);
				float overSpend = admTransLossService.selectTransLossSumByTransDate(vo.getReportDate());
				vo.setOverPriceSum(df2.format(overSpend));
			}

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4, 0, 0, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdActionReportPdf(doc, dataList, startDate, endDate,
					adTypeText, customerInfoId, pfdCustomerInfoIdText, payTypeText);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}

    public String downloadAdActionExcel() throws Exception {
        log.info(">>> startDate = " + startDate);
        log.info(">>> endDate = " + endDate);
        log.info(">>> adType = " + adType);
        log.info(">>> customerInfoId = " + customerInfoId);
        log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
        log.info(">>> payType = " + payType);
        log.info(">>> pfdCustomerInfoIdText = " + pfdCustomerInfoIdText);
        log.info(">>> payTypeText = " + payTypeText);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            String adTypeStr = getAdTypeSelectOptionsMap().get(adType);
            if (StringUtils.isBlank(adTypeStr)) {
                adTypeStr = "全部";
            }
            String pfdCustomerInfoIdStr = "";
            if (StringUtils.isBlank(pfdCustomerInfoId)) {
                pfdCustomerInfoIdStr = "全部";
            }
            else {
                for (PfdCustomerInfo pfdCustomerInfo: getCompanyList()) {
                    if (pfdCustomerInfo.getCustomerInfoId().equals(pfdCustomerInfoId)) {
                        pfdCustomerInfoIdStr = pfdCustomerInfo.getCompanyName();
                        break;
                    }
                }
            }
            String payTypeStr = getPayTypeMap().get(payType);
            if (StringUtils.isBlank(payTypeStr)) {
                payTypeStr = "全部";
            }

            // 舊版，讀取 pfp_ad_pvclk
            //this.dataList = adReportService.getAdActionReportList(startDate, endDate, adStyle, customerInfoId, adType);
            // 新版，讀取 pfd_ad_action_report，忽略廣告樣式，但是加入經銷商代碼
            this.dataList = adReportService.getAdActionReportList(startDate, endDate,
                    customerInfoId, adType, pfdCustomerInfoId, payType);

            log.info(">>> dataList.size() = " + dataList.size());

            if (dataList.size()==0) {
                this.message = "查無資料！";
                return INPUT;
            }

            //超播金額
            DecimalFormat df2 = new DecimalFormat("###,###,###,###");
            for (int i=0; i<dataList.size(); i++) {
                AdActionReportVO vo = dataList.get(i);
                float overSpend = admTransLossService.selectTransLossSumByTransDate(vo.getReportDate());
                vo.setOverPriceSum(df2.format(overSpend));
            }

            // excel
            downloadFileName = URLEncoder.encode("總廣告成效_" + dateFormat.format(new Date()) + ".xls", "UTF-8");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            WritableWorkbook book = Workbook.createWorkbook(out);
            WritableSheet sheet = book.createSheet("Sheet1", 0);

            sheet.addCell(new Label(0, 0, "報表類型：總廣告成效"));
            sheet.addCell(new Label(0, 2, "廣告形式：" + adTypeStr));
            sheet.addCell(new Label(0, 3, "經銷歸屬：" + pfdCustomerInfoIdStr));
            sheet.addCell(new Label(0, 4, "付款方式：" + payTypeStr));
            sheet.addCell(new Label(0, 5, "查詢日期：" + startDate + " 到 " + endDate));

            sheet.addCell(new Label(0, 7, "日期"));
            sheet.addCell(new Label(1, 7, "曝光數"));
            sheet.addCell(new Label(2, 7, "互動數"));
            sheet.addCell(new Label(3, 7, "互動率"));
            sheet.addCell(new Label(4, 7, "單次互動費用"));
            sheet.addCell(new Label(5, 7, "千次曝光費用"));
            sheet.addCell(new Label(6, 7, "費用"));
            sheet.addCell(new Label(7, 7, "超播金額"));

            int lineNo = 8;
            for (AdActionReportVO data: dataList) {
                sheet.addCell(new Label(0, lineNo, data.getReportDate()));
                sheet.addCell(new Label(1, lineNo, data.getPvSum()));
                sheet.addCell(new Label(2, lineNo, data.getClkSum()));
                sheet.addCell(new Label(3, lineNo, data.getClkRate()));
                sheet.addCell(new Label(4, lineNo, data.getClkPriceAvg()));
                sheet.addCell(new Label(5, lineNo, data.getPvPriceAvg()));
                sheet.addCell(new Label(6, lineNo, data.getPriceSum()));
                sheet.addCell(new Label(7, lineNo, data.getOverPriceSum()));

                lineNo++;
            }

            book.write();
            book.close();
            downloadFileStream = new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return SUCCESS;
    }

	/**
	 * 查詢 AdActionReportDetail(總廣告成效明細表) 資料
	 * @return String 廣告成效列表
	 */
	public String adActionReportDetail() throws Exception {

		log.info(">>> reportDate = " + reportDate);
		log.info(">>> adType = " + adType);
		log.info(">>> customerInfoId = " + customerInfoId);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		//this.dataList = adReportService.getAdActionReportDetail(reportDate, reportDate, adStyle, customerInfoId, adType);
		this.dataList = adReportService.getAdActionReportDetail(reportDate, reportDate,
				customerInfoId, adType, pfdCustomerInfoId, payType);

		//超播金額
		Map<String, Double> overSpendMap = admTransLossService.selectTransLossSumByTransDateMap(reportDate);

		DecimalFormat df2 = new DecimalFormat("###,###,###,###");

		for (int i=0; i<dataList.size(); i++) {
			AdActionReportVO vo = dataList.get(i);
			String customerInfoId = vo.getCustomerInfoId();
			long overSpend = 0;
			if (overSpendMap.containsKey(customerInfoId)) {
				overSpend = overSpendMap.get(customerInfoId).intValue();
			}
			vo.setOverPriceSum(df2.format(overSpend));
		}

		return SUCCESS;
	}

	/**
	 * 下載 AdActionReportDetail(總廣告成效明細表) 資料
	 * @return String 廣告成效列表
	 */
	public String downloadAdActionDetail() throws Exception {

		log.info(">>> reportDate = " + reportDate);
		log.info(">>> adType = " + adType);
		log.info(">>> customerInfoId = " + customerInfoId);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName =  URLEncoder.encode("總廣告成效明細_" + reportNo + ".pdf", "UTF-8");

		try {

			//this.dataList = adReportService.getAdActionReportDetail(reportDate, reportDate, adStyle, customerInfoId, adType);
			this.dataList = adReportService.getAdActionReportDetail(reportDate, reportDate,
					customerInfoId, adType, pfdCustomerInfoId, payType);

	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

			//超播金額
			Map<String, Double> overSpendMap = admTransLossService.selectTransLossSumByTransDateMap(reportDate);

			DecimalFormat df2 = new DecimalFormat("###,###,###,###");

			for (int i=0; i<dataList.size(); i++) {
				AdActionReportVO vo = dataList.get(i);
				String customerInfoId = vo.getCustomerInfoId();
				long overSpend = 0;
				if (overSpendMap.containsKey(customerInfoId)) {
					overSpend = overSpendMap.get(customerInfoId).intValue();
				}
				vo.setOverPriceSum(df2.format(overSpend));
			}

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4.rotate(), -20, -20, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdActionDetailPdf(doc, dataList, reportDate,
					adTypeText, customerInfoId,	pfdCustomerInfoId, payType);

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

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public void setAdmTransLossService(IAdmTransLossService admTransLossService) {
		this.admTransLossService = admTransLossService;
	}

	public String getPfdCustomerInfoIdText() {
		return pfdCustomerInfoIdText;
	}

	public void setPfdCustomerInfoIdText(String pfdCustomerInfoIdText) {
		this.pfdCustomerInfoIdText = pfdCustomerInfoIdText;
	}

	public String getPayTypeText() {
		return payTypeText;
	}

	public void setPayTypeText(String payTypeText) {
		this.payTypeText = payTypeText;
	}

	public String getAdTypeText() {
		return adTypeText;
	}

	public void setAdTypeText(String adTypeText) {
		this.adTypeText = adTypeText;
	}

	public Map<String, String> getPayTypeMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "全部");
		map.put(EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfdAccountPayType.ADVANCE.getPayName());
		map.put(EnumPfdAccountPayType.LATER.getPayType(), EnumPfdAccountPayType.LATER.getPayName());
		return map;
	}

	public InputStream getDownloadFileStream() {
	    return downloadFileStream;
	}

	public void setDownloadFileStream(InputStream downloadFileStream) {
	    this.downloadFileStream = downloadFileStream;
	}

	public String getDownloadFileName() {
	    return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
	    this.downloadFileName = downloadFileName;
	}


	public AdmRecognizeRecordService getAdmRecognizeRecordService() {
	    return admRecognizeRecordService;
	}


	public void setAdmRecognizeRecordService(
		AdmRecognizeRecordService admRecognizeRecordService) {
	    this.admRecognizeRecordService = admRecognizeRecordService;
	}



}
