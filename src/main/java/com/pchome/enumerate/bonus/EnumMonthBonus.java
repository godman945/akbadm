package com.pchome.enumerate.bonus;

/**
 * 是否當月獎金
 * 要與前台(pfd) EnumMonthBonus 一致
 */
public enum EnumMonthBonus {
	
	YES("Y"),
	NO("N");
	
	private final String status;
	
	private EnumMonthBonus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	
}
