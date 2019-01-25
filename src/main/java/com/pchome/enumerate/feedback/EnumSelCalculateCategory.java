package com.pchome.enumerate.feedback;

/**
 * 回饋金加值狀態
 */
public enum EnumSelCalculateCategory {

	QUARTZS("1","排程加值"),
	NOW("2","立即加值");	
	
	private final String category;
	private final String chName;
	
	private EnumSelCalculateCategory(String category, String chName) {
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
