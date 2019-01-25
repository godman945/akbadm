package com.pchome.enumerate.manager;

public enum EnumManagerStatus {

	START("1","開啟"),
	CLOSE("0","關閉"),	
	DELETE("2","刪除");
	
	private final String status;
	private final String chName;
	
	private EnumManagerStatus(String status, String chName) {
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
