package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbApplyStatus {

	APPLY("1","申請中"),
	APPLYFAIL("4","申請失敗"),
	WAIT_PAY("2","付款中"),
	SUCCESS("3","付款成功"),
	PAYFAIL("5","付款失敗");
	

	private final String status;
	private final String chName;
	
	private EnumPfbApplyStatus(String status, String chName) {
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
