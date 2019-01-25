package com.pchome.akbadm.db.vo;

/**
 * 結算 VO
 * 
 */

public class SettlementVO {

	private String customerInfoId;
	private float transPrice = 0;
	private float totalAddMoney = 0;
	private float totalRetrieve = 0;
	private float totalSpend = 0;
	private float tax = 0;
	private float remain = 0;
	private float totalLaterAddMoney = 0;
	private float totalLaterSpend = 0;	
	private float totalLaterRetrieve = 0;
	//private float laterRemain = 0;
	
	
	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public float getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(float transPrice) {
		this.transPrice = transPrice;
	}
	
	public float getTotalAddMoney() {
		return totalAddMoney;
	}
	
	public void setTotalAddMoney(float totalAddMoney) {
		this.totalAddMoney = totalAddMoney;
	}
	
	public float getTotalRetrieve() {
		return totalRetrieve;
	}
	
	public void setTotalRetrieve(float totalRetrieve) {
		this.totalRetrieve = totalRetrieve;
	}
	
	public float getTotalSpend() {
		return totalSpend;
	}
	
	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public void setTotalSpend(float totalSpend) {
		this.totalSpend = totalSpend;
	}
	
	public float getRemain() {
		return remain;
	}
	
	public void setRemain(float remain) {
		this.remain = remain;
	}
	
	public float getTotalLaterAddMoney() {
		return totalLaterAddMoney;
	}
	
	public void setTotalLaterAddMoney(float totalLaterAddMoney) {
		this.totalLaterAddMoney = totalLaterAddMoney;
	}
	
	public float getTotalLaterSpend() {
		return totalLaterSpend;
	}
	
	public void setTotalLaterSpend(float totalLaterSpend) {
		this.totalLaterSpend = totalLaterSpend;
	}
	
	public float getTotalLaterRetrieve() {
		return totalLaterRetrieve;
	}

	public void setTotalLaterRetrieve(float totalLaterRetrieve) {
		this.totalLaterRetrieve = totalLaterRetrieve;
	}

//	public float getLaterRemain() {
//		return laterRemain;
//	}
//	
//	public void setLaterRemain(float laterRemain) {
//		this.laterRemain = laterRemain;
//	}
	
}
