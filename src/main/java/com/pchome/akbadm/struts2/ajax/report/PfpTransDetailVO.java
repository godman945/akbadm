package com.pchome.akbadm.struts2.ajax.report;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PfpTransDetailVO {

	private String transDate; //交易日期
	private String custName; //客戶名稱
	private String custId; //客戶帳號
	private int add; //帳戶儲值
	private int tax; //稅額
	private int spend; //廣告花費
	private int invalid; //惡意點擊費用
	private int refund; //退款
	private int gift; //禮金贈送
	private int remain; //餘額

	private String add_display; //帳戶儲值顯示
	private String tax_display; //稅額顯示
	private String spend_display; //廣告花費顯示
	private String invalid_display; //惡意點擊費用顯示
	private String refund_display; //退款顯示
	private String gift_display; //禮金贈送顯示
	private String remain_display; //餘額顯示

	private String pfdCustInfoName; //PFD公司名
	private String pfdUserName; //PFD使用者
	private String payType; //付款方式

	NumberFormat numFormat = new DecimalFormat("###,###,###,###");

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getSpend() {
		return spend;
	}

	public void setSpend(int spend) {
		this.spend = spend;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public int getRefund() {
		return refund;
	}

	public void setRefund(int refund) {
		this.refund = refund;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

	public String getAdd_display() {
		return numFormat.format(add);
	}

	public String getSpend_display() {
		return numFormat.format(spend);
	}

	public String getInvalid_display() {
		return numFormat.format(invalid);
	}

	public String getRefund_display() {
		return numFormat.format(refund);
	}

	public String getRemain_display() {
		return numFormat.format(remain);
	}

	public int getGift() {
		return gift;
	}

	public void setGift(int gift) {
		this.gift = gift;
	}

	public String getGift_display() {
		return numFormat.format(gift);
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public String getTax_display() {
		return numFormat.format(tax);
	}

	public String getPfdCustInfoName() {
		return pfdCustInfoName;
	}

	public void setPfdCustInfoName(String pfdCustInfoName) {
		this.pfdCustInfoName = pfdCustInfoName;
	}

	public String getPfdUserName() {
		return pfdUserName;
	}

	public void setPfdUserName(String pfdUserName) {
		this.pfdUserName = pfdUserName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
}
