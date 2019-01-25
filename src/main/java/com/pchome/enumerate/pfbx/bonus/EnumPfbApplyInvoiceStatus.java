package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbApplyInvoiceStatus {

	VERIFY("1","待補件"),
	SUCCESS("2","審核成功"),
	FAIL("3","審核失敗");
	
	private final String status;
	private final String chName;
	
	private EnumPfbApplyInvoiceStatus(String status, String chName) {
		this.status = status;
		this.chName = chName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getChName() {
		return chName;
	}
}
