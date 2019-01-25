package com.pchome.enumerate.pfbx.account;

public enum EnumPfbAccountStatus {

	APPLY("0", "申請中"),
	START("1", "開通"),
	CLOSE("2", "關閉"),
	STOP("3", "封鎖"),
	DELETE("4", "刪除"),
	FAIL("5", "審核失敗");

	private final String status;
	private final String description;
	
	private EnumPfbAccountStatus(String status, String description) {
		this.status = status;
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}
}
