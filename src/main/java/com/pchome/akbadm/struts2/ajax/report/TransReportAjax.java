package com.pchome.akbadm.struts2.ajax.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.trans.PfpTransDetailService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.akbadm.utils.PdfUtil;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class TransReportAjax extends BaseCookieAction{

	private static final long serialVersionUID = -1572086139145889336L;

	private PfpTransDetailService pfpTransDetailService;

	private String fontPath;	// 字形檔參數
	
	//頁面參數
	private String startDate;
	private String endDate;
	private String customerInfo;
	private String pfdCustomerInfoId;
	private String payType;
	private String pfdCustomerInfoIdText;
	private String payTypeText;

	private List<PfpTransDetailVO> dataList;
	
	private String totalAddMoney; //帳戶儲值加總
	private String totalTax; //稅額加總
	private String totalSpend; //廣告花費加總
	private String totalInvalidCost; //惡意點擊費用加總
	private String totalRefund; //退款加總
	private String totalGift; //禮金加總

	private String fileName;
	private InputStream pdfStream = null;

	private String message = "";
	
	public String searchTransReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfpCustomerInfo = " + customerInfo);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {

			List<PfpTransDetail> transDetailList = pfpTransDetailService.findTransDetail(customerInfo,
					startDate, endDate, pfdCustomerInfoId);

			if (transDetailList==null || transDetailList.size()==0) {

				this.message = "查無資料！";

			} else {

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				NumberFormat numFormat = new DecimalFormat("###,###,###,###");

				Map<String, String> payTypeMap = new HashMap<String, String>();
				for (EnumPfdAccountPayType _payType : EnumPfdAccountPayType.values()) {
					payTypeMap.put(_payType.getPayType(), _payType.getPayName());
				}

				Map<String, PfpTransDetailVO> map = new LinkedHashMap<String, PfpTransDetailVO>();

				int _totalAddMoney = 0; //帳戶儲值加總
				int _totalTax = 0; //稅額加總
				int _totalSpend = 0; //廣告花費加總
				int _totalInvalidCost = 0; //惡意點擊費用加總
				int _totalRefund = 0; //退款加總
				int _totalGift = 0; //禮金加總

				for (PfpTransDetail transDetail: transDetailList) {

					String _pfdCustInfoName = "";
					String _pfdUserName = "";
					String _payType = "";
					if (StringUtils.isNotBlank(payType)) {

						//一定要是 PFD 所管轄的 PFP 才會有付款方式條件
						if (transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().size()==0) {
							continue;
						}

						PfdUserAdAccountRef pfdUserAdAccountRef = (PfdUserAdAccountRef) transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().toArray()[0];

						//非使用者要查詢的付款方式 -> 排除
						if (!payType.equals(pfdUserAdAccountRef.getPfpPayType())) {
							continue;
						}

						_pfdCustInfoName = pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName();
						_pfdUserName = pfdUserAdAccountRef.getPfdUser().getUserName();
						_payType = payTypeMap.get(pfdUserAdAccountRef.getPfpPayType());

					} else {

						if (transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().size()>0) {
							PfdUserAdAccountRef pfdUserAdAccountRef = (PfdUserAdAccountRef) transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().toArray()[0];
							_pfdCustInfoName = pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName();
							_pfdUserName = pfdUserAdAccountRef.getPfdUser().getUserName();
							_payType = payTypeMap.get(pfdUserAdAccountRef.getPfpPayType());
						}
					}

					String transDate = dateFormat.format(transDetail.getTransDate());
					String custName = transDetail.getPfpCustomerInfo().getCustomerInfoTitle();
					String custId = transDetail.getPfpCustomerInfo().getCustomerInfoId();
					String transType = transDetail.getTransType();

					int transPrice = (int) transDetail.getTransPrice();
					int transTax = (int) transDetail.getTax();
					int remain = (int) transDetail.getRemain();

					String key = transDate + custId;

					PfpTransDetailVO vo = null;
					int add = 0; //帳戶儲值
					int tax = 0; //稅額
					int spend = 0; //廣告花費
					int invalid = 0; //惡意點擊費用
					int refund = 0; //退款
					int gift = 0; //禮金贈送
					if (map.containsKey(key)) {
						vo = map.get(key);

						add = vo.getAdd();
						tax = vo.getTax();
						spend = vo.getSpend();
						invalid = vo.getInvalid();
						refund = vo.getRefund();
						gift = vo.getGift();

					} else {

						vo = new PfpTransDetailVO();
						vo.setTransDate(transDate);
						vo.setCustName(custName);
						vo.setCustId(custId);
						vo.setRemain(remain);
						vo.setPfdCustInfoName(_pfdCustInfoName);
						vo.setPfdUserName(_pfdUserName);
						vo.setPayType(_payType);
					}

					if (transType.equals(EnumTransType.ADD_MONEY.getTypeId())) {
						add += transPrice;
						_totalAddMoney += transPrice;
						tax += transTax;
						_totalTax += transTax;
					} else if (transType.equals(EnumTransType.SPEND_COST.getTypeId())) {
						spend += transPrice;
						_totalSpend += transPrice;
					} else if (transType.equals(EnumTransType.INVALID_COST.getTypeId())) {
						invalid += transPrice;
						_totalInvalidCost += transPrice;
					} else if (transType.equals(EnumTransType.REFUND.getTypeId())) {
						refund += transPrice;
						_totalRefund += transPrice;
					} else if (transType.equals(EnumTransType.GIFT.getTypeId())) {
						gift += transPrice;
						_totalGift += transPrice;
					} else if(transType.equals(EnumTransType.LATER_SAVE.getTypeId())){
						add += transPrice;
						_totalAddMoney += transPrice;
					} else if(transType.equals(EnumTransType.LATER_REFUND.getTypeId())){
						refund += transPrice;
						_totalRefund += transPrice;
					}

					vo.setAdd(add);
					vo.setTax(tax);
					vo.setSpend(spend);
					vo.setInvalid(invalid);
					vo.setRefund(refund);
					vo.setGift(gift);
					vo.setRemain(remain);

					map.put(key, vo);
				}

				//有可能過濾完都沒資料了
				if (map.size()==0) {

					this.message = "查無資料！";

				} else {

					dataList = new ArrayList<PfpTransDetailVO>(map.values());

					totalAddMoney = numFormat.format(_totalAddMoney);
					totalTax = numFormat.format(_totalTax);
					totalSpend = numFormat.format(_totalSpend);
					totalInvalidCost = numFormat.format(_totalInvalidCost);
					totalRefund = numFormat.format(_totalRefund);
					totalGift = numFormat.format(_totalGift);
					
				}
			}
		}
		
		return SUCCESS;
	}
	
	public String downloadTransReportAjax() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> customerInfo = " + customerInfo);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> payType = " + payType);
		log.info(">>> pfdCustomerInfoIdText = " + pfdCustomerInfoIdText);
		log.info(">>> payTypeText = " + payTypeText);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName = URLEncoder.encode("帳戶交易明細報表_" + reportNo + ".pdf", "UTF-8");

		Map<String, String> payTypeMap = new HashMap<String, String>();
		for (EnumPfdAccountPayType _payType : EnumPfdAccountPayType.values()) {
			payTypeMap.put(_payType.getPayType(), _payType.getPayName());
		}

		try {

			List<PfpTransDetail> transDetailList = pfpTransDetailService.findTransDetail(customerInfo,
					startDate, endDate, pfdCustomerInfoId);
	        log.info(">>> transDetailList.size() = " + transDetailList.size());

	        Map<String, PfpTransDetailVO> map = new LinkedHashMap<String, PfpTransDetailVO>();

			for (PfpTransDetail transDetail: transDetailList) {

				String _pfdCustInfoName = "";
				String _pfdUserName = "";
				String _payType = "";
				if (StringUtils.isNotBlank(payType)) {

					//一定要是 PFD 所管轄的 PFP 才會有付款方式條件
					if (transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().size()==0) {
						continue;
					}

					PfdUserAdAccountRef pfdUserAdAccountRef = (PfdUserAdAccountRef) transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().toArray()[0];

					//非使用者要查詢的付款方式 -> 排除
					if (!payType.equals(pfdUserAdAccountRef.getPfpPayType())) {
						continue;
					}

					_pfdCustInfoName = pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName();
					_pfdUserName = pfdUserAdAccountRef.getPfdUser().getUserName();
					_payType = payTypeMap.get(pfdUserAdAccountRef.getPfpPayType());

				} else {

					if (transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().size()>0) {
						PfdUserAdAccountRef pfdUserAdAccountRef = (PfdUserAdAccountRef) transDetail.getPfpCustomerInfo().getPfdUserAdAccountRefs().toArray()[0];
						_pfdCustInfoName = pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName();
						_pfdUserName = pfdUserAdAccountRef.getPfdUser().getUserName();
						_payType = payTypeMap.get(pfdUserAdAccountRef.getPfpPayType());
					}
				}

				String transDate = dateFormat.format(transDetail.getTransDate());
				String custName = transDetail.getPfpCustomerInfo().getCustomerInfoTitle();
				String custId = transDetail.getPfpCustomerInfo().getCustomerInfoId();
				String transType = transDetail.getTransType();
				int transPrice = (int) transDetail.getTransPrice();
				int transTax = (int) transDetail.getTax();
				int remain = (int) transDetail.getRemain();

				String key = transDate + custId;

				PfpTransDetailVO vo = null;
				int add = 0; //帳戶儲值
				int tax = 0; //稅額
				int spend = 0; //廣告花費
				int invalid = 0; //惡意點擊費用
				int refund = 0; //退款
				int gift = 0; //禮金贈送

				if (map.containsKey(key)) {

					vo = map.get(key);

					add = vo.getAdd();
					tax = vo.getTax();
					spend = vo.getSpend();
					invalid = vo.getInvalid();
					refund = vo.getRefund();
					gift = vo.getGift();

				} else {

					vo = new PfpTransDetailVO();
					vo.setTransDate(transDate);
					vo.setCustName(custName);
					vo.setCustId(custId);
					vo.setRemain(remain);
					vo.setPfdCustInfoName(_pfdCustInfoName);
					vo.setPfdUserName(_pfdUserName);
					vo.setPayType(_payType);

				}

				if (transType.equals(EnumTransType.ADD_MONEY.getTypeId())) {
					add += transPrice;
					tax += transTax;
				} else if (transType.equals(EnumTransType.SPEND_COST.getTypeId())) {
					spend += transPrice;
				} else if (transType.equals(EnumTransType.INVALID_COST.getTypeId())) {
					invalid += transPrice;
				} else if (transType.equals(EnumTransType.REFUND.getTypeId())) {
					refund += transPrice;
				} else if (transType.equals(EnumTransType.GIFT.getTypeId())) {
					gift += transPrice;
				} else if (transType.equals(EnumTransType.LATER_SAVE.getTypeId())) {
					add += transPrice;
				} else if (transType.equals(EnumTransType.LATER_REFUND.getTypeId())) {
					refund += transPrice;
				}

				vo.setAdd(add);
				vo.setTax(tax);
				vo.setSpend(spend);
				vo.setInvalid(invalid);
				vo.setRefund(refund);
				vo.setGift(gift);
				vo.setRemain(remain);

				map.put(key, vo);
			}

			dataList = new ArrayList<PfpTransDetailVO>(map.values());

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4.rotate(), -20, -20, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareTransDetailReportPdf(doc, dataList, startDate, endDate, customerInfo,
					pfdCustomerInfoIdText, payTypeText);

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

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getTotalAddMoney() {
		return totalAddMoney;
	}

	public String getTotalTax() {
		return totalTax;
	}

	public String getTotalRefund() {
		return totalRefund;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getPdfStream() {
		return pdfStream;
	}

	public String getTotalSpend() {
		return totalSpend;
	}

	public String getTotalInvalidCost() {
		return totalInvalidCost;
	}

	public String getTotalGift() {
		return totalGift;
	}

	public List<PfpTransDetailVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PfpTransDetailVO> dataList) {
		this.dataList = dataList;
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
}
