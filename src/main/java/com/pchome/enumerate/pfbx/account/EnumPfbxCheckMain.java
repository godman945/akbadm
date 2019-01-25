package com.pchome.enumerate.pfbx.account;

public enum EnumPfbxCheckMain {

	MAIN("Y", "主要"),
	SLAVE("N", "次要");

	private final String code;
	private final String cName;
	
	private EnumPfbxCheckMain(String code, String cName) {
		this.code = code;
		this.cName = cName;
	}

	public String getCode()
	{
		return code;
	}

	public String getcName()
	{
		return cName;
	}
	
}
