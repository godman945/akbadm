package com.pchome.enumerate.account;

public enum EnumAccountStatus {

	CLOSE("0","關閉"),
	START("1","開啟"),
	STOP("2","停權"),
	DELETE("3","刪除"),
	APPLY("4","申請中");
	
	private final String status;
	private final String description;
	
	private EnumAccountStatus(String status, String description) {
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
