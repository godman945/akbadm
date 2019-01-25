package com.pchome.enumerate.freeAction;

/**
 * 活動序號使用狀態
 */
public enum EnumGiftSnoUsed {

	YES("Y","序號已啟用"),
	NO("N","序號未啟用");
	
	private final String status;
	private final String chName;
	
	private EnumGiftSnoUsed(String status, String chName) {
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
