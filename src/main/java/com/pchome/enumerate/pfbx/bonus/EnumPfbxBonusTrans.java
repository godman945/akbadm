package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbxBonusTrans {

	BONUS_START("1","本月開始可分潤餘額"),
	ADD_EXPENSE("2","新增分潤"),
	APPLY_PROCESS("3","收益請款"),
	APPLY_SUCCESS("4","請款完成"),
	APPLY_FALE("5","請款失敗"),
	BONUS_ADD("6","收益調整增加"),
	BONUS_LESS("7","收益調整減少"),
	INVALID_TRAFFIC("8","無效流量");
	
	
	
	
	
	private final String type;
	private final String desc;
	
	private EnumPfbxBonusTrans(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
}
