package com.pchome.akbadm.db.vo.manager;

import java.util.List;
import java.util.Map;

public class ManagerPfpVO {

	private String pfdCustomerInfoId;
	private String pfdCustomerInfoName;
	
	private List<Map<String,String>> list;

	
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

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
	

	
}
