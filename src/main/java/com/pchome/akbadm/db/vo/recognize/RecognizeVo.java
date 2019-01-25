package com.pchome.akbadm.db.vo.recognize;

public class RecognizeVo {

	private String orderType;			// 訂單類型
	private String typeChName;			// 訂單名稱	
	private int amount;					// 目前筆數
	private float totalSave;			// 總加值金額(未稅)
	private float totalSpend;			// 總花費金額(未稅)
	private float totalRemain;			// 總餘額(未稅)
	private float totalTaxSave;			// 總加值金額(含稅)
	private float totalTaxSpend;		// 總花費金額(含稅)
	private float totalTaxRemain;		// 總餘額(含稅)
	private float totalRefundPrice;		// 總退款(未稅)
	private float totalRefundPriceTax;	// 總退款(含稅)
	
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTypeChName() {
		return typeChName;
	}
	public void setTypeChName(String typeChName) {
		this.typeChName = typeChName;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public float getTotalSave() {
		return totalSave;
	}
	public void setTotalSave(float totalSave) {
		this.totalSave = totalSave;
	}
	public float getTotalSpend() {
		return totalSpend;
	}
	public void setTotalSpend(float totalSpend) {
		this.totalSpend = totalSpend;
	}
	public float getTotalRemain() {
		return totalRemain;
	}
	public void setTotalRemain(float totalRemain) {
		this.totalRemain = totalRemain;
	}
	public float getTotalTaxSave() {
		return totalTaxSave;
	}
	public void setTotalTaxSave(float totalTaxSave) {
		this.totalTaxSave = totalTaxSave;
	}
	public float getTotalTaxSpend() {
		return totalTaxSpend;
	}
	public void setTotalTaxSpend(float totalTaxSpend) {
		this.totalTaxSpend = totalTaxSpend;
	}
	public float getTotalTaxRemain() {
		return totalTaxRemain;
	}
	public void setTotalTaxRemain(float totalTaxRemain) {
		this.totalTaxRemain = totalTaxRemain;
	}
	public float getTotalRefundPrice() {
		return totalRefundPrice;
	}
	public void setTotalRefundPrice(float totalRefundPrice) {
		this.totalRefundPrice = totalRefundPrice;
	}
	public float getTotalRefundPriceTax() {
		return totalRefundPriceTax;
	}
	public void setTotalRefundPriceTax(float totalRefundPriceTax) {
		this.totalRefundPriceTax = totalRefundPriceTax;
	}
	
}
