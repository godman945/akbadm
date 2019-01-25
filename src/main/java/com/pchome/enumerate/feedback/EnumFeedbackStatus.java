package com.pchome.enumerate.feedback;

public enum EnumFeedbackStatus {
	
	DELECT("0","已刪除"),
	START("1","使用中");
	
	private final String status;
	private final String dec;	
	
	private EnumFeedbackStatus(String status, String dec) {
		this.status = status;
		this.dec = dec;
	}
	
	public String getStatus() {
		return status;
	}

	public String getDec() {
		return dec;
	}
	
}
