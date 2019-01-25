package com.pchome.akbadm.db.vo.manager;

import java.util.List;
import java.util.Map;

public class ManagerPfbVO {

	private String pfbCustomerInfoId;
	private String pfbCustomerInfoName;
	private String isChecked;
	
	private List<Map<String,String>> list;

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

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	
}
