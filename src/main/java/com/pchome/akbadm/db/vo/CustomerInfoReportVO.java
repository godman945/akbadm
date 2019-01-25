package com.pchome.akbadm.db.vo;

public class CustomerInfoReportVO {

	private String activateDate;
	private String memberId;
	private String customerInfoId;
	private String customerInfoName;
	private String customerInfoType;
	private String orderType;
	private String transType;
	private String transContent;
	private int addMoney;
	private int tax;
	private String pfdCustInfoName;
	private String pfdUserName;
	private String payType;
	
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
	public String getActivateDate() {
		return activateDate;
	}
	public void setActivateDate(String activateDate) {
		this.activateDate = activateDate;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public void setCustomerInfoName(String customerInfoName) {
		this.customerInfoName = customerInfoName;
	}
	public String getMemberId() {
		return memberId;
	}
	public String getCustomerInfoName() {
		return customerInfoName;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getCustomerInfoType() {
		return customerInfoType;
	}
	public void setCustomerInfoType(String customerInfoType) {
		this.customerInfoType = customerInfoType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransContent() {
		return transContent;
	}
	public void setTransContent(String transContent) {
		this.transContent = transContent;
	}
	public int getAddMoney() {
		return addMoney;
	}
	public void setAddMoney(int addMoney) {
		this.addMoney = addMoney;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	
}
