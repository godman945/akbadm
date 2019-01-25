package com.pchome.enumerate.pfbx.account;

public enum EnumPfbxCheckStatus {

	ING("1", "待補件"),
	OK("2" , "審核成功"),
	FAIL("3", "審核失敗");

	private final String status;
	private final String cName;
	
	private EnumPfbxCheckStatus(String status, String cName) {
		this.status = status;
		this.cName = cName;
	}

	public String getStatus()
	{
		return status;
	}

	public String getcName()
	{
		return cName;
	}
	
}
