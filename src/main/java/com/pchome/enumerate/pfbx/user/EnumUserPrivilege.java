package com.pchome.enumerate.pfbx.user;

public enum EnumUserPrivilege {

	ROOT_USER(0, "總管理者"),
	ADM_USER(1, "管理全帳戶"),
	AD_USER(2, "播放管理、成效管理、帳款管理"),
	REPORT_USER(3, "成效管理、帳款管理"),
	BILL_USER(4, "帳款管理");

	private final Integer privilegeId;
	private final String chName;

	private EnumUserPrivilege(Integer privilegeId, String chName){
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
