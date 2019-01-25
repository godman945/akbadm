package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbCloseDay {

	DAY_25(25),
	DAY_26(26);		// 結算day// 結算day
	
	private final int day;

	private EnumPfbCloseDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}
}
