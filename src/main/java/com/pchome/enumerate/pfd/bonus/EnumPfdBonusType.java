package com.pchome.enumerate.pfd.bonus;

public enum EnumPfdBonusType {

	BONUS_FIXED("1","達成獎金"),
	BONUS_DEVELOP("2","開發獎金"),
	BONUS_CASE("3","專案獎金");
	
	private final String type;
	private final String chName;
	
	private EnumPfdBonusType(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}
		
}
