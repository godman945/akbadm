package com.pchome.akbadm.db.vo.manager;

import java.util.List;

public class ManagerVO {

	private String system;
	private String systemId;
	private String name;
	private String memberId;
	private String privilege;
	private String status;
	private List<String> customerInfoIds;
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getSystemId() {
		return systemId;
	}
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPrivilege() {
		return privilege;
	}
	
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getCustomerInfoIds() {
		return customerInfoIds;
	}

	public void setCustomerInfoIds(List<String> customerInfoIds) {
		this.customerInfoIds = customerInfoIds;
	}
	
}
