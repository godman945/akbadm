package com.pchome.enumerate.convert;

public enum EnumConvertType {
	StandardConvert("1","標準轉換追蹤"),
	CustomConvert("2","自訂轉換追蹤");
	
	private final String type;
	private final String chName;
	
	private EnumConvertType(String type, String chName) {
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
