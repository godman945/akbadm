package com.pchome.enumerate.retrieve;

public enum EnumRetrieveRecordType {

	GIFT("G", "廣告金"),	
	FEEDBACK("F", "回饋金");

	private final String code;
	private final String chName;

	private EnumRetrieveRecordType(String code, String chName) {
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
