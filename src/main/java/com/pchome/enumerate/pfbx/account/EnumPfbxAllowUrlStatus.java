package com.pchome.enumerate.pfbx.account;

public enum EnumPfbxAllowUrlStatus {

	APPLY("0", "審核中"),
	FAIL("1","審核失敗"),
	START("2", "開通"),
	STOP("3", "封鎖");
	
	private final String code;
	private final String chName;
	
	private EnumPfbxAllowUrlStatus(String code, String chName) {
		this.code = code;
		this.chName = chName;
	}

	public String getCode() {
		return code;
	}

	public String getChName() {
		return chName;
	}
	
}
