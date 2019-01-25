package com.pchome.enumerate.manager;

/**
 * 頻道種類
 */
public enum EnumChannelCategory {

	PFP("0","PFP"),
	PFD("1","PFD"),
	PFB("2","PFB");
	
	private final String category;
	private final String chName;
	
	private EnumChannelCategory(String category, String chName) {
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
