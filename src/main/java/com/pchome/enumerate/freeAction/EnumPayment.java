package com.pchome.enumerate.freeAction;

/**
 * 付款狀態
 */
public enum EnumPayment {

	YES("Y","要儲值"),
	NO("N","免儲值");
	
	private final String status;
	private final String chName;
	
	private EnumPayment(String status, String chName) {
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
