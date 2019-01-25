package com.pchome.enumerate.report;

public enum EnumAdAgeCode {

	A("A","18歲(不含)以下"),
	B("B","18歲~24歲"),
	C("C","25歲~34歲"),
	D("D","35歲~44歲"),
	E("E","45歲~54歲"),
	F("F","55歲~64歲"),
	G("G","65~74歲"),
	H("H","75歲以上"),
	I("I","不分年齡");
	
	private final String code;
	private final String name;
	
	private EnumAdAgeCode(String code, String name){
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
