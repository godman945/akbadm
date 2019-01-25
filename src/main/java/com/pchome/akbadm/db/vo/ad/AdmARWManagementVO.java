package com.pchome.akbadm.db.vo.ad;

public class AdmARWManagementVO {
	
	private String pfdCustomerInfoId; //PFD 經銷商帳號
	private String customerInfoId; // PFP帳戶編號
	private String customerInfoTitle; // PFP帳戶名稱
	private int arwValue; // ARW權重值
	private int dateFlag; // 走期狀態。 0:走期無限 1:檢查走期
	private String startDate; // 走期開始日期
	private String endDate; // 走期結束日期
	
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	
	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}
	
	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}
	
	public int getArwValue() {
		return arwValue;
	}
	
	public void setArwValue(int arwValue) {
		this.arwValue = arwValue;
	}
	
	public int getDateFlag() {
		return dateFlag;
	}
	
	public void setDateFlag(int dateFlag) {
		this.dateFlag = dateFlag;
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
	
}
