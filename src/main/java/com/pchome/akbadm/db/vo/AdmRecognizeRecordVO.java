package com.pchome.akbadm.db.vo;

public class AdmRecognizeRecordVO {

	private String saveDate;
	private String memberId;
	private String customerInfoTitle;
	private String orderType;
	private String orderPrice;
	private String tax;
	private String totalOrderPrice;
	private String pfdCustInfoTitle;
	private String pfdUserName;
	private String payType;

	public String getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}

	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getPfdCustInfoTitle() {
		return pfdCustInfoTitle;
	}

	public void setPfdCustInfoTitle(String pfdCustInfoTitle) {
		this.pfdCustInfoTitle = pfdCustInfoTitle;
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

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTotalOrderPrice() {
		return totalOrderPrice;
	}

	public void setTotalOrderPrice(String totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}
	
}
