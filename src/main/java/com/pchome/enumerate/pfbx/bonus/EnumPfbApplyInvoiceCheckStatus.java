package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbApplyInvoiceCheckStatus {

	WAIT("1","待確認"),
	SUCCESS("2","確認成功");


	private final String status;
	private final String chName;

	private EnumPfbApplyInvoiceCheckStatus(String status, String chName) {
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
