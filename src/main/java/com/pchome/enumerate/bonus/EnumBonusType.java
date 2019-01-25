package com.pchome.enumerate.bonus;

public enum EnumBonusType {

	BONUS_FIXED("1","達成獎金"),
	BONUS_DEVELOP("2","開發獎金"),
	BONUS_CASE("3","專案獎金");
	
	private final String typeId;
	private final String typeName;
	
	
	private EnumBonusType(String typeId, String typeName) {
		this.typeId = typeId;
		this.typeName = typeName;
	}
	
	public String getTypeId() {
		return typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	
	
}
