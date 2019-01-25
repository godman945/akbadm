package com.pchome.rmi.manager;

import java.io.Serializable;

public class PfbAccountVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String pfbCustomerInfoId;
	private String pfbCustomerInfoName;
	private String pfbCustomerInfoStatus;
	
	public String getPfbCustomerInfoId() {
		return pfbCustomerInfoId;
	}
	
	public void setPfbCustomerInfoId(String pfbCustomerInfoId) {
		this.pfbCustomerInfoId = pfbCustomerInfoId;
	}
	
	public String getPfbCustomerInfoName() {
		return pfbCustomerInfoName;
	}
	
	public void setPfbCustomerInfoName(String pfbCustomerInfoName) {
		this.pfbCustomerInfoName = pfbCustomerInfoName;
	}
	
	public String getPfbCustomerInfoStatus() {
		return pfbCustomerInfoStatus;
	}
	
	public void setPfbCustomerInfoStatus(String pfbCustomerInfoStatus) {
		this.pfbCustomerInfoStatus = pfbCustomerInfoStatus;
	}
}
