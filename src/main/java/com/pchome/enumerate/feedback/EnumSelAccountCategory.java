package com.pchome.enumerate.feedback;

/**
 * 回饋金加值帳戶類型
 */
public enum EnumSelAccountCategory {

	ONLY("1","各別帳戶"),
	ALL("2","所有帳戶");
	
	private final String category;
	private final String chName;
	
	private EnumSelAccountCategory(String category, String chName) {
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
