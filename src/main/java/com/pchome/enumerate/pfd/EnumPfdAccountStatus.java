package com.pchome.enumerate.pfd;

/**
 * 帳戶狀態
 * 需與 PFD 前台 EnumAccountStatus 同步 
 */
public enum EnumPfdAccountStatus {

	APPLY("0", "申請中"),
	START("1", "開啟"),
	CLOSE("2", "關閉"),
	STOP("3", "暫停"),
	DELETE("4", "刪除");
	
	private final String status;
	private final String chName;
	
	private EnumPfdAccountStatus(String status, String chName) {
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
