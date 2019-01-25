package com.pchome.enumerate.manager;
/**
 * 管理者權限
 * 1. 總管理權限只能由RD手動建立，無法從系統建立
 * 2. 總管理權限只需要建立 adm_manager_detail
 */
public enum EnumManagerPrivilege {

	ROOT("0","總管理",true),
	FINANCE("1","財務",false),
	AM("2","AM",false),
	AE("3","AE",false);
	
	private final String privilege;
	private final String chName;
	private final boolean searchAll;
	
	private EnumManagerPrivilege(String privilege, String chName, boolean searchAll) {
		this.privilege = privilege;
		this.chName = chName;
		this.searchAll = searchAll;
	}

	public String getPrivilege() {
		return privilege;
	}

	public String getChName() {
		return chName;
	}

	public boolean isSearchAll() {
		return searchAll;
	}
}
