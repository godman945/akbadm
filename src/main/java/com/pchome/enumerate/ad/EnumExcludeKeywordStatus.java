package com.pchome.enumerate.ad;

public enum EnumExcludeKeywordStatus {
	
	CLOSE(0),	// 關閉
	START(1),	// 開啟
	STOP(2);	// 暫停
	
	private final int statusId;

	private EnumExcludeKeywordStatus(int statusId) {
		this.statusId = statusId;
	}

	public int getStatusId() {
		return statusId;
	}
	
	
	
}
