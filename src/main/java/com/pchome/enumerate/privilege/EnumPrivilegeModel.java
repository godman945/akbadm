package com.pchome.enumerate.privilege;

public enum EnumPrivilegeModel {
	
	ROOT_USER(0,"總管理者"),
	ADM_USER(1,"管理全帳戶"),
	AD_USER(2,"廣告、報管、帳單管理"),
	REPORT_USER(3,"報表、帳單管理"),
	BILL_USER(4,"帳單管理");
	
	private final Integer privilegeId;
	private final String chName;
	
	
	private EnumPrivilegeModel(Integer privilegeId, String chName){
		this.privilegeId = privilegeId;
		this.chName = chName;		
	}

	public Integer getPrivilegeId() {
		return privilegeId;
	}
	
	public String getChName() {
		return chName;
	}




}
