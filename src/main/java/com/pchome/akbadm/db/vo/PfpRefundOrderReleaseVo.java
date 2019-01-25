package com.pchome.akbadm.db.vo;

public class PfpRefundOrderReleaseVo {
	
	private String releaseSeq;			//退款審核序號
	private String customerInfoId;		//pfp帳號
	private String billingId;			//金流訂單編號
	private String notifyDate;			//金流系統訂單日期
	private float orderPriceTax;		//金流系統所帶入的訂單金額(含稅)(order_price+tax)
	private String billingPayStatus;	//金流付款方式(信用卡、ATM)
	private String billingStatus;		//金流狀態(付款完成or退款)
	private float orderRemainTax;		//訂單可退餘額(含稅)(order_remain+tax_remain)
	private String refundStatus;		//退款狀態
	private String applyTime;			//申請退款時間
	private String releaseStatus;		//退款審核狀態(審核狀態 y:未審核 p:審核通過 r:拒絕退款)
	private String customerInfoTitle;	//pfp名稱
	private String overOneDay;			//申請退款日是否超過一天(Y or N)
	private String rejectReason;		//拒絕理由
	private float refundPriceTax;		//退款金額(含稅)
	
	public String getReleaseSeq() {
		return releaseSeq;
	}
	public void setReleaseSeq(String releaseSeq) {
		this.releaseSeq = releaseSeq;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getBillingId() {
		return billingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	public String getNotifyDate() {
		return notifyDate;
	}
	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}
	public float getOrderPriceTax() {
		return orderPriceTax;
	}
	public void setOrderPriceTax(float orderPriceTax) {
		this.orderPriceTax = orderPriceTax;
	}
	public String getBillingPayStatus() {
		return billingPayStatus;
	}
	public void setBillingPayStatus(String billingPayStatus) {
		this.billingPayStatus = billingPayStatus;
	}
	public String getBillingStatus() {
		return billingStatus;
	}
	public void setBillingStatus(String billingStatus) {
		this.billingStatus = billingStatus;
	}
	public float getOrderRemainTax() {
		return orderRemainTax;
	}
	public void setOrderRemainTax(float orderRemainTax) {
		this.orderRemainTax = orderRemainTax;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getReleaseStatus() {
		return releaseStatus;
	}
	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	}
	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}
	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}
	public String getOverOneDay() {
		return overOneDay;
	}
	public void setOverOneDay(String overOneDay) {
		this.overOneDay = overOneDay;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public float getRefundPriceTax() {
		return refundPriceTax;
	}
	public void setRefundPriceTax(float refundPriceTax) {
		this.refundPriceTax = refundPriceTax;
	}
	
}
