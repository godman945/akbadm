package com.pchome.enumerate.report;

public enum EnumVerifyStatusType {
	Unverified("0", "未認證"), 
	Verified("1", "已認證");

	private final String type;
	private final String chName;

	private EnumVerifyStatusType(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}

}
