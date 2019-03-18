package com.pchome.enumerate.report;

public enum EnumRetargetingCodeType {
	GeneralWebTracking("0", "一般網頁追蹤"), 
	DynamicProductAdTracking("1", "動態商品廣告追蹤");

	private final String type;
	private final String chName;

	private EnumRetargetingCodeType(String type, String chName) {
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
