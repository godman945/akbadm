package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbxBonusSet {

	OPEN("1","開啟"),
	CLOSE("2","關閉");
	
	private final String status;
	private final String chName;
	
	private EnumPfbxBonusSet(String status, String chName) {
		this.status = status;
		this.chName = chName;
	}

	public String getStatus() {
		return status;
	}

	public String getChName() {
		return chName;
	}
}
