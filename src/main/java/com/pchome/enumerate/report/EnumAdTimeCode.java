package com.pchome.enumerate.report;

public enum EnumAdTimeCode {

	A("A","0-3時"),
	B("B","4-7時"),
	C("C","8-11時"),
	D("D","12-15時"),
	E("E","16-19時"),
	F("F","20-23時");
	
	private final String code;
	private final String name;
	
	private EnumAdTimeCode(String code, String name){
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
