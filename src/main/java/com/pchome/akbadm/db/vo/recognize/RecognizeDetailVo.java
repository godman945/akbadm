package com.pchome.akbadm.db.vo.recognize;

public class RecognizeDetailVo {

	private String spendDate;		// 攤提日期
	private String pfpId;			// 帳戶編號
	private String orderId;			// 訂單編號
	private String typChName;		// 訂單類型
	private float orderPrice;		// 訂單金額(未稅)
	private float spendCost;		// 廣告花費(未稅)
	private float refund;			// 退款金額(未稅)
	private float remain;			// 剩餘金額(未稅)
	private float orderTaxPrice;	// 訂單金額(含稅)
	private float spendTaxCost;		// 廣告花費(含稅)
	private float taxRemain;		// 剩餘金額(含稅)
	private String note;			// 備註	
	
	public String getSpendDate() {
		return spendDate;
	}

	public void setSpendDate(String spendDate) {
		this.spendDate = spendDate;
	}

	public String getPfpId() {
		return pfpId;
	}

	public void setPfpId(String pfpId) {
		this.pfpId = pfpId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTypChName() {
		return typChName;
	}

	public void setTypChName(String typChName) {
		this.typChName = typChName;
	}

	public float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public float getSpendCost() {
		return spendCost;
	}

	public void setSpendCost(float spendCost) {
		this.spendCost = spendCost;
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

	public float getRemain() {
		return remain;
	}

	public void setRemain(float remain) {
		this.remain = remain;
	}

	public float getOrderTaxPrice() {
		return orderTaxPrice;
	}

	public void setOrderTaxPrice(float orderTaxPrice) {
		this.orderTaxPrice = orderTaxPrice;
	}

	public float getSpendTaxCost() {
		return spendTaxCost;
	}

	public void setSpendTaxCost(float spendTaxCost) {
		this.spendTaxCost = spendTaxCost;
	}

	public float getTaxRemain() {
		return taxRemain;
	}

	public void setTaxRemain(float taxRemain) {
		this.taxRemain = taxRemain;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
