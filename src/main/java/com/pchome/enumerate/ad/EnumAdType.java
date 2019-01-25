package com.pchome.enumerate.ad;

public enum EnumAdType {
	
	AD_ALL(0,"搜尋廣告+聯播網廣告"),					// 全部廣告
	AD_SEARCH(1,"搜尋廣告"),				// 搜尋廣告
	AD_CHANNEL(2,"聯播網廣告");			// 聯播廣告
	
	private final int type;
	private final String chName;
	
	private EnumAdType(int type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public int getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}

	
}
