package com.pchome.enumerate.pfd;

public enum EnumPfdContractStatus {

	START("1", "合約有效"),
	CLOSE("2", "合約終止"),
	EARLY_CLOSE("3", "合約提前中止");
	
	
	private final String statusId;
	private final String chName;
	
	private EnumPfdContractStatus(String statusId, String chName) {
		this.statusId = statusId;
		this.chName = chName;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getChName() {
		return chName;
	}
	
}
