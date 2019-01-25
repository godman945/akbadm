package com.pchome.enumerate.pfd.bonus;

public enum EnumPfdCloseDay {

	DAY_25(25);
	
	private final int day;

	private EnumPfdCloseDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}
}
