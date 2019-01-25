package com.pchome.enumerate.pfd;

/**
 * PFD 帳戶類型
 */
public enum EnumPfdCategory {

	DEALER("0","經銷商"),
	AGENT("1","代理商");
	
	private final String category;
	private final String chName;
	
	private EnumPfdCategory(String category, String chName) {
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
