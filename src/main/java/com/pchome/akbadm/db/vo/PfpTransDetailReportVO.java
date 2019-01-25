package com.pchome.akbadm.db.vo;

public class PfpTransDetailReportVO {

	private String reportDate; //日期
	private double add; //加值
	private double tax; //稅
	private double spend; //花費
	private double invalid; //惡意點擊
	private double refund; //惡意點擊
	private double free; //免費贈送
	private String customerInfoId; //帳戶ID(明細頁面)
	private String customerInfoName; //帳戶姓名(明細頁面)

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public double getAdd() {
		return add;
	}

	public void setAdd(double add) {
		this.add = add;
	}

	public double getSpend() {
		return spend;
	}

	public void setSpend(double spend) {
		this.spend = spend;
	}

	public double getInvalid() {
		return invalid;
	}

	public void setInvalid(double invalid) {
		this.invalid = invalid;
	}

	public double getRefund() {
		return refund;
	}

	public void setRefund(double refund) {
		this.refund = refund;
	}

	public double getFree() {
		return free;
	}

	public void setFree(double free) {
		this.free = free;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getCustomerInfoName() {
		return customerInfoName;
	}

	public void setCustomerInfoName(String customerInfoName) {
		this.customerInfoName = customerInfoName;
	}
}
