package com.pchome.enumerate.pfd;

/**
 * 帳號狀態
 * 需與 PFD 前台 EnumUserStatus 同步 
 */
public enum EnumPfdUserStatus {
	
	APPLY("0", "申請中"), //ROOT 申請後的初始狀態
	INVITE("1", "邀請中"),
	START("2", "啟用"),
	CLOSE("3", "關閉"),
	STOP("4", "停權"),
	DELETE("5", "刪除"),
	INVALID("6","無效帳號");	

	private final String status;
	private final String chName;

	private EnumPfdUserStatus(String status, String chName) {
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
