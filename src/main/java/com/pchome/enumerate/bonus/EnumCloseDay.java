package com.pchome.enumerate.bonus;

/**
 * 要與前台(pfd) EnumCloseDay 一致
 */
public enum EnumCloseDay {
	
	CLOSE_DAY(25);
	
	private final int day;

	private EnumCloseDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}
}
