package com.pchome.akbadm.db.vo.pfd.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfdContract;

public class PfdBonusRecordInvoiceVO {

	private Integer id;
	private PfdContract pfdContract;
	private String payType;
	private String bonusItem;
	private int quarter;
	private float nowClkPrice;
	private float nowBonus;
	private Date recordDate;
	private int closeYear;
	private int closeMonth;
	private String bonusType;
	private String download;
	private String billStatus;
	private String billNote;
	private String financeInvoiceSno;
	private Date financeInvoiceDate;
	private Float financeInvoiceMoney;
	private Date financePayDate;
	private String pfdInvoiceSno;
	private Date pfdInvoiceDate;
	private Float pfdInvoiceMoney;
	private String pfdPayCategory;
	private String pfdCheckSno;
	private Date pfdCheckCloseDate;
	private Float debitMoney;
	private Date debitDate;
	private Date balanceDate;
	private Integer payId;
	private Date createDate;
	private Date updateDate;
	private String thisMonth;
	private String pfdContractId;
	private String pfpPayType;
	private String pfdCustomerInfoId;
	private String companyName;
	private String companyTaxId;
	private float laterAdClkPrice;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public PfdContract getPfdContract() {
		return pfdContract;
	}
	public void setPfdContract(PfdContract pfdContract) {
		this.pfdContract = pfdContract;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBonusItem() {
		return bonusItem;
	}
	public void setBonusItem(String bonusItem) {
		this.bonusItem = bonusItem;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public int getCloseYear() {
		return closeYear;
	}
	public void setCloseYear(int closeYear) {
		this.closeYear = closeYear;
	}
	public int getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}
	public float getNowClkPrice() {
		return nowClkPrice;
	}
	public void setNowClkPrice(float nowClkPrice) {
		this.nowClkPrice = nowClkPrice;
	}
	public float getNowBonus() {
		return nowBonus;
	}
	public void setNowBonus(float nowBonus) {
		this.nowBonus = nowBonus;
	}
	public String getBonusType() {
		return bonusType;
	}
	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getBillNote() {
		return billNote;
	}
	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}
	public String getFinanceInvoiceSno() {
		return financeInvoiceSno;
	}
	public void setFinanceInvoiceSno(String financeInvoiceSno) {
		this.financeInvoiceSno = financeInvoiceSno;
	}
	public Date getFinanceInvoiceDate() {
		return financeInvoiceDate;
	}
	public void setFinanceInvoiceDate(Date financeInvoiceDate) {
		this.financeInvoiceDate = financeInvoiceDate;
	}
	public Float getFinanceInvoiceMoney() {
		return financeInvoiceMoney;
	}
	public void setFinanceInvoiceMoney(Float financeInvoiceMoney) {
		this.financeInvoiceMoney = financeInvoiceMoney;
	}
	public Date getFinancePayDate() {
		return financePayDate;
	}
	public void setFinancePayDate(Date financePayDate) {
		this.financePayDate = financePayDate;
	}
	public String getPfdInvoiceSno() {
		return pfdInvoiceSno;
	}
	public void setPfdInvoiceSno(String pfdInvoiceSno) {
		this.pfdInvoiceSno = pfdInvoiceSno;
	}
	public Date getPfdInvoiceDate() {
		return pfdInvoiceDate;
	}
	public void setPfdInvoiceDate(Date pfdInvoiceDate) {
		this.pfdInvoiceDate = pfdInvoiceDate;
	}
	public Float getPfdInvoiceMoney() {
		return pfdInvoiceMoney;
	}
	public void setPfdInvoiceMoney(Float pfdInvoiceMoney) {
		this.pfdInvoiceMoney = pfdInvoiceMoney;
	}
	public String getPfdPayCategory() {
		return pfdPayCategory;
	}
	public void setPfdPayCategory(String pfdPayCategory) {
		this.pfdPayCategory = pfdPayCategory;
	}
	public String getPfdCheckSno() {
		return pfdCheckSno;
	}
	public void setPfdCheckSno(String pfdCheckSno) {
		this.pfdCheckSno = pfdCheckSno;
	}
	public Date getPfdCheckCloseDate() {
		return pfdCheckCloseDate;
	}
	public void setPfdCheckCloseDate(Date pfdCheckCloseDate) {
		this.pfdCheckCloseDate = pfdCheckCloseDate;
	}
	public Float getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(Float debitMoney) {
		this.debitMoney = debitMoney;
	}
	public Date getDebitDate() {
		return debitDate;
	}
	public void setDebitDate(Date debitDate) {
		this.debitDate = debitDate;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Integer getPayId() {
		return payId;
	}
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getThisMonth() {
		return thisMonth;
	}
	public void setThisMonth(String thisMonth) {
		this.thisMonth = thisMonth;
	}
	public String getPfdContractId() {
		return pfdContractId;
	}
	public void setPfdContractId(String pfdContractId) {
		this.pfdContractId = pfdContractId;
	}
	public String getPfpPayType() {
		return pfpPayType;
	}
	public void setPfpPayType(String pfpPayType) {
		this.pfpPayType = pfpPayType;
	}
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyTaxId() {
		return companyTaxId;
	}
	public void setCompanyTaxId(String companyTaxId) {
		this.companyTaxId = companyTaxId;
	}
	public float getLaterAdClkPrice() {
		return laterAdClkPrice;
	}
	public void setLaterAdClkPrice(float laterAdClkPrice) {
		this.laterAdClkPrice = laterAdClkPrice;
	}
}
