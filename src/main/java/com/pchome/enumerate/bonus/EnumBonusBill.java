package com.pchome.enumerate.bonus;

/**
 * 對帳單類型
 */
public enum EnumBonusBill {

	MONTH_DETAIL_BILL("1",new int[]{}),							// 月對帳單
	QUARTER_DETAIL_BILL("2",new int[]{3, 6, 9, 12}),			// 季對帳單
	YEAR_DETAIL_BILL("3",new int[]{12});						// 年對帳單
	
	private final String type;
	private final int[] month; 

	private EnumBonusBill(String type, int[] month) {
		this.type = type;
		this.month = month;
	}

	public String getType() {
		return type;
	}

	public int[] getMonth() {
		return month;
	}
	
}
