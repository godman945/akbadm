package com.pchome.akbadm.db.vo;

public class OrderReportVO {
	
	private String orderDate;
	private String memberId;
	private String customerInfoName;
	private int addMoney;
	
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCustomerInfoName() {
		return customerInfoName;
	}
	public void setCustomerInfoName(String customerInfoName) {
		this.customerInfoName = customerInfoName;
	}
	public int getAddMoney() {
		return addMoney;
	}
	public void setAddMoney(int addMoney) {
		this.addMoney = addMoney;
	}
		
}
