package com.pchome.akbadm.struts2.action.bonus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

//import com.pchome.akbadm.db.pojo.PfdMonthTotalBonus;
//import com.pchome.akbadm.db.service.bonus.IPfdMonthTotalBonusService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.bonus.EnumInvoicPayType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class BalanceInvoiceAction extends BaseCookieAction{

	private static final String FILE_TYPE = ".csv";
	
	//private IPfdMonthTotalBonusService pfdMonthTotalBonusService;
	
	private String invoiceStrDate;
	private String invoiceEndDate;
	private String pfdCustomerInfoId;
	
	private EnumPfdAccountPayType[] enumPfdAccountPayType = EnumPfdAccountPayType.values();				// 帳戶付款方式
	private EnumInvoicPayType[] enumInvoicPayType = EnumInvoicPayType.values();							// 發票付款方式
	//private List<PfdMonthTotalBonus> monthTotalBonusInvoices;
	
	private String downloadFileName;								// 下載檔案名稱
	private InputStream downloadFileStream;							// 下載檔案
	
	public String balanceInvoiceReportAction(){

		
		this.queryBalanceInvoiceReport();
	
		return SUCCESS;
	}
	
	public String donwnloadBalanceReportAction() throws Exception{
		
		this.queryBalanceInvoiceReport();
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append("沖帳報表");
		fileName.append("_");
		fileName.append(DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH));
		fileName.append(FILE_TYPE);
		
		StringBuffer content = new StringBuffer();									
		
		content.append("帳務月份,經銷商編號,經銷商名稱,統一編號,付款狀態,廣告點擊費用,應付獎金,");
		content.append("發票號碼,發票日期,發票金額,付款日期,");
		content.append("發票號碼,發票日期,發票金額,付款方式,支票號碼,支票到期日期,");
		content.append("折讓金額	,折讓日期,沖帳日期");
		content.append("\n");	  
		
		/*
		for(PfdMonthTotalBonus invoice:monthTotalBonusInvoices){
			content.append(invoice.getYear()+"-"+invoice.getMonth()).append(",");
			//content.append(invoice.getPfdCustomerInfo().getCustomerInfoId()).append(",");
			//content.append(invoice.getPfdCustomerInfo().getCompanyName()).append(",");
			//content.append(invoice.getPfdCustomerInfo().getCompanyTaxId()).append(",");
			content.append(enumPfdAccountPayType[1].getPayName()).append(",");
			content.append(invoice.getTotalAdClickCost()).append(",");
			content.append(invoice.getBonusMoney()).append(",");
			content.append(invoice.getFinanceInvoiceSno()).append(",");
			content.append(invoice.getFinanceInvoiceDate()).append(",");
			content.append(invoice.getFinanceInvoiceMoney()).append(",");
			content.append(invoice.getFinancePayDate()).append(",");
			content.append(invoice.getAdInvoiceSno()).append(",");
			content.append(invoice.getAdInvoiceDate()).append(",");
			content.append(invoice.getAdInvoiceMoney()).append(",");
			
			for(EnumInvoicPayType payType:enumInvoicPayType){
				if(payType.getType().equals(invoice.getAdPayType())){
					content.append(payType.getChName()).append(",");
				}
			}			
			
			content.append(invoice.getCheckSno()).append(",");
			content.append(invoice.getCheckClosingDate()).append(",");
			content.append(invoice.getDebitMoney()).append(",");
			content.append(invoice.getDebitDate()).append(",");
			content.append(invoice.getBalanceDate());
			content.append("\n");
		}
	    */
		
		downloadFileName = URLEncoder.encode(fileName.toString(), "UTF-8");
		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));	
		
		return SUCCESS;
	}
	
	private void queryBalanceInvoiceReport(){
		
		log.info("invoiceStrDate: "+invoiceStrDate);
		log.info("invoiceEndDate: "+invoiceEndDate);
		log.info("pfdCustomerInfoId: "+pfdCustomerInfoId);
		
		String strYear = invoiceStrDate.split("-")[0];
		String strMonth = invoiceStrDate.split("-")[1];
		String endYear = invoiceEndDate.split("-")[0];
		String endMonth = invoiceEndDate.split("-")[1];
		String startDate = DateValueUtil.getInstance().getFirstDayOfMonth(Integer.parseInt(strYear), Integer.parseInt(strMonth));
		String endDate = DateValueUtil.getInstance().getLastDayOfMonth(Integer.parseInt(endYear), Integer.parseInt(endMonth));
	
		//monthTotalBonusInvoices = pfdMonthTotalBonusService.findBalanceInvoiceReport(pfdCustomerInfoId, startDate, endDate);
	}
	
	//public void setPfdMonthTotalBonusService(
			//IPfdMonthTotalBonusService pfdMonthTotalBonusService) {
		//this.pfdMonthTotalBonusService = pfdMonthTotalBonusService;
	//}

	public void setInvoiceStrDate(String invoiceStrDate) {
		this.invoiceStrDate = invoiceStrDate;
	}

	public void setInvoiceEndDate(String invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getInvoiceStrDate() {
		return invoiceStrDate;
	}

	public String getInvoiceEndDate() {
		return invoiceEndDate;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public EnumPfdAccountPayType[] getEnumPfdAccountPayType() {
		return enumPfdAccountPayType;
	}

	public EnumInvoicPayType[] getEnumInvoicPayType() {
		return enumInvoicPayType;
	}
	
	//public List<PfdMonthTotalBonus> getMonthTotalBonusInvoices() {
		//return monthTotalBonusInvoices;
	//}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}
	
}
