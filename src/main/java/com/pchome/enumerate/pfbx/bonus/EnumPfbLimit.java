package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbLimit {

	MIN(500,"最少申請金額");
	
	private final float money;
	private final String chName;
	
	private EnumPfbLimit(float money, String chName) {
		this.money = money;
		this.chName = chName;
	}

	public float getMoney() {
		return money;
	}

	public String getChName() {
		return chName;
	}

}
