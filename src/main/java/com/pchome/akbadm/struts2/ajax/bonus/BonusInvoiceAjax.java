package com.pchome.akbadm.struts2.ajax.bonus;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

//import com.pchome.akbadm.db.pojo.PfdMonthTotalBonus;
//import com.pchome.akbadm.db.service.bonus.IPfdMonthTotalBonusService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.bonus.EnumApplyBonusStatus;
import com.pchome.enumerate.bonus.EnumInvoicPayType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class BonusInvoiceAjax extends BaseCookieAction{
	/*

	private IPfdMonthTotalBonusService pfdMonthTotalBonusService;
	
	private String invoiceStrDate;
	private String invoiceEndDate;
	private String pfdCustomerInfoId;
	private String bonusInvoiceId;
	
	private List<PfdMonthTotalBonus> monthTotalBonusInvoices;
	private PfdMonthTotalBonus monthTotalBonusInvoice;
	private EnumPfdAccountPayType[] enumPfdAccountPayType = EnumPfdAccountPayType.values();				// 帳戶付款方式
	private EnumInvoicPayType[] enumInvoicPayType = EnumInvoicPayType.values();							// 發票付款方式
	
	private String financeInvoiceSno;					// 財務發票號碼
	private String financeInvoiceDate;					// 財務發票日期
	private float financeInvoiceMoney;					// 財務發票金額
	private String financePayDate;						// 財務付款日期
	private String adInvoiceSno;						// 廣告商發票號碼
	private String adInvoiceDate;						// 廣告商發票日期
	private float adInvoiceMoney;						// 廣告商發票金額
	private String adPayType;							// 廣告商付款方式
	private String checkSno;							// 廣告商支票號碼
	private String checkClosingDate;					// 廣告商支票到期日期
	
	
	public String searchBonusInvoiceAjax() {
		
		//log.info(" invoiceStrDate: "+invoiceStrDate);
		//log.info(" invoiceEndDate: "+invoiceEndDate);
		//log.info(" pfdCustomerInfoId: "+pfdCustomerInfoId);
		
		String strYear = invoiceStrDate.split("-")[0];
		String strMonth = invoiceStrDate.split("-")[1];
		String endYear = invoiceEndDate.split("-")[0];
		String endMonth = invoiceEndDate.split("-")[1];
		String startDate = DateValueUtil.getInstance().getFirstDayOfMonth(Integer.parseInt(strYear), Integer.parseInt(strMonth));
		String endDate = DateValueUtil.getInstance().getLastDayOfMonth(Integer.parseInt(endYear), Integer.parseInt(endMonth));
		
		monthTotalBonusInvoices = pfdMonthTotalBonusService.findPfdMonthTotalBonusInvoice(pfdCustomerInfoId, startDate, endDate);
		
		//log.info(" size: "+pfdBonusInvoices.size());
		
		return SUCCESS;
	}

	public String modifyBonusInvoiceAjax() {
		
		//log.info(" bonusInvoiceId: "+bonusInvoiceId);
		
		monthTotalBonusInvoice = pfdMonthTotalBonusService.findPfdMonthTotalBonusInvoice(Integer.parseInt(bonusInvoiceId));
		
		return SUCCESS;
	}
	
	public String updateBonusInvoiceAjax() {
		
		//log.info(" bonusInvoiceId: "+bonusInvoiceId);
		//log.info(" financeInvoiceSno: "+financeInvoiceSno);
		//log.info(" financeInvoiceMoney: "+financeInvoiceMoney);
		//log.info(" financeInvoiceDate: "+financeInvoiceDate);
		//log.info(" financePayDate: "+financePayDate);
		
		monthTotalBonusInvoice = pfdMonthTotalBonusService.findPfdMonthTotalBonusInvoice(Integer.parseInt(bonusInvoiceId));				
		
		if(monthTotalBonusInvoice != null){
			
			if(StringUtils.isNotBlank(financeInvoiceSno) && StringUtils.isNotBlank(financeInvoiceDate) && StringUtils.isNotBlank(financePayDate)){
				monthTotalBonusInvoice.setFinanceInvoiceSno(financeInvoiceSno);
				monthTotalBonusInvoice.setFinanceInvoiceMoney(financeInvoiceMoney);
				monthTotalBonusInvoice.setFinanceInvoiceDate(DateValueUtil.getInstance().stringToDate(financeInvoiceDate));
				monthTotalBonusInvoice.setFinancePayDate(DateValueUtil.getInstance().stringToDate(financePayDate));
				monthTotalBonusInvoice.setBillingStatus(EnumApplyBonusStatus.YES.getApplyStatus());
			}
			
			if(StringUtils.isNotBlank(adInvoiceSno) && StringUtils.isNotBlank(adInvoiceDate) && StringUtils.isNotBlank(adPayType)
					&& StringUtils.isNotBlank(checkSno) && StringUtils.isNotBlank(checkClosingDate)){
				
				monthTotalBonusInvoice.setAdInvoiceSno(adInvoiceSno);
				monthTotalBonusInvoice.setAdInvoiceDate(DateValueUtil.getInstance().stringToDate(adInvoiceDate));
				monthTotalBonusInvoice.setAdInvoiceMoney(adInvoiceMoney);
				monthTotalBonusInvoice.setAdPayType(adPayType);
				monthTotalBonusInvoice.setCheckSno(checkSno);
				monthTotalBonusInvoice.setCheckClosingDate(DateValueUtil.getInstance().stringToDate(checkClosingDate));
			}
			
			monthTotalBonusInvoice.setUpdateDate(new Date());
			
			pfdMonthTotalBonusService.saveOrUpdate(monthTotalBonusInvoice);
		}
		
		
		return SUCCESS;
	}

	public String updateBalanceInvoiceAjax() {
		
		monthTotalBonusInvoice = pfdMonthTotalBonusService.findPfdMonthTotalBonusInvoice(Integer.parseInt(bonusInvoiceId));
		
		if(monthTotalBonusInvoice != null){
			monthTotalBonusInvoice.setBalanceDate(new Date());
			monthTotalBonusInvoice.setUpdateDate(new Date());
			pfdMonthTotalBonusService.saveOrUpdate(monthTotalBonusInvoice);
		}
		
		return SUCCESS;
	}
	
	public void setPfdMonthTotalBonusService(
			IPfdMonthTotalBonusService pfdMonthTotalBonusService) {
		this.pfdMonthTotalBonusService = pfdMonthTotalBonusService;
	}

	public void setInvoiceStrDate(String invoiceStrDate) {
		this.invoiceStrDate = invoiceStrDate;
	}

	public void setInvoiceEndDate(String invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public void setBonusInvoiceId(String bonusInvoiceId) {
		this.bonusInvoiceId = bonusInvoiceId;
	}

	public List<PfdMonthTotalBonus> getMonthTotalBonusInvoices() {
		return monthTotalBonusInvoices;
	}

	public PfdMonthTotalBonus getMonthTotalBonusInvoice() {
		return monthTotalBonusInvoice;
	}

	public EnumPfdAccountPayType[] getEnumPfdAccountPayType() {
		return enumPfdAccountPayType;
	}

	public EnumInvoicPayType[] getEnumInvoicPayType() {
		return enumInvoicPayType;
	}

	public void setFinanceInvoiceSno(String financeInvoiceSno) {
		this.financeInvoiceSno = financeInvoiceSno;
	}

	public void setFinanceInvoiceDate(String financeInvoiceDate) {
		this.financeInvoiceDate = financeInvoiceDate;
	}

	public void setFinanceInvoiceMoney(float financeInvoiceMoney) {
		this.financeInvoiceMoney = financeInvoiceMoney;
	}

	public void setFinancePayDate(String financePayDate) {
		this.financePayDate = financePayDate;
	}

	public void setAdInvoiceSno(String adInvoiceSno) {
		this.adInvoiceSno = adInvoiceSno;
	}

	public void setAdInvoiceDate(String adInvoiceDate) {
		this.adInvoiceDate = adInvoiceDate;
	}

	public void setAdInvoiceMoney(float adInvoiceMoney) {
		this.adInvoiceMoney = adInvoiceMoney;
	}

	public void setAdPayType(String adPayType) {
		this.adPayType = adPayType;
	}

	public void setCheckSno(String checkSno) {
		this.checkSno = checkSno;
	}

	public void setCheckClosingDate(String checkClosingDate) {
		this.checkClosingDate = checkClosingDate;
	}	
*/
}
