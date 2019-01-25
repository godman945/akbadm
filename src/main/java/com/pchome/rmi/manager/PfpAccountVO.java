package com.pchome.rmi.manager;

import java.io.Serializable;

public class PfpAccountVO implements Serializable{

	private String customerInfoId;
	private String memberId;
	private String customerInfoName;
	private String customerInfoStatus;
	private String customerInfoPayType;
	private String managerMemberId;
	private String managerName;
	private String pfdCustomerInfoId;
	private String pfdCustomerInfoName;
	private float oneWeekAdCost;
	private float customerInfoRemain;
	
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
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

	public String getCustomerInfoStatus() {
		return customerInfoStatus;
	}

	public void setCustomerInfoStatus(String customerInfoStatus) {
		this.customerInfoStatus = customerInfoStatus;
	}

	public String getCustomerInfoPayType() {
		return customerInfoPayType;
	}

	public void setCustomerInfoPayType(String customerInfoPayType) {
		this.customerInfoPayType = customerInfoPayType;
	}

	public String getManagerMemberId() {
		return managerMemberId;
	}

	public void setManagerMemberId(String managerMemberId) {
		this.managerMemberId = managerMemberId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getPfdCustomerInfoName() {
		return pfdCustomerInfoName;
	}

	public void setPfdCustomerInfoName(String pfdCustomerInfoName) {
		this.pfdCustomerInfoName = pfdCustomerInfoName;
	}

	public float getOneWeekAdCost() {
		return oneWeekAdCost;
	}

	public void setOneWeekAdCost(float oneWeekAdCost) {
		this.oneWeekAdCost = oneWeekAdCost;
	}

	public float getCustomerInfoRemain() {
		return customerInfoRemain;
	}

	public void setCustomerInfoRemain(float customerInfoRemain) {
		this.customerInfoRemain = customerInfoRemain;
	}
	
}
