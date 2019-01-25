package com.pchome.enumerate.pfbx.account;

public enum EnumPfbxAccountCategory {

	PERSONAL("1", "個人戶"),
	FIRM("2", "公司戶");

	private final String category;
	private final String chName;
	
	private EnumPfbxAccountCategory(String category, String chName) {
		this.category = category;
		this.chName = chName;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getChName() {
		return chName;
	}
	
}
