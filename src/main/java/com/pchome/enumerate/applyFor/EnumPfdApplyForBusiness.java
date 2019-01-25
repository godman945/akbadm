package com.pchome.enumerate.applyFor;

public enum EnumPfdApplyForBusiness {

	YET("y", "申請中"),
	PASS("p", "核准開發"),
	REJECT("r", "申請拒絕");

	private final String type;
	private final String chName;

	private EnumPfdApplyForBusiness(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}
}
