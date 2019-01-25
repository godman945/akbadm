package com.pchome.enumerate.freeAction;

/**
 * 活動序號使用頁面
 */
public enum EnumGiftStyle {

	REGISTER("1","帳戶註冊頁"),
	GIFT("2","帳戶儲值頁");
	
	private final String status;
	private final String chName;
	
	private EnumGiftStyle(String status, String chName) {
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
