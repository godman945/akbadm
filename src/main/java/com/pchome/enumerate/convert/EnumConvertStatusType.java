package com.pchome.enumerate.convert;

public enum EnumConvertStatusType {
	Close("0","關閉"),		
	Open("1","開啟"),
	Delete("2","刪除");
	
	private final String type;
	private final String chName;
	
	private EnumConvertStatusType(String type, String chName) {
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
