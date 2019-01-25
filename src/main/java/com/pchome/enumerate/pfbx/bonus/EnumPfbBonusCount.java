package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbBonusCount {

	PFD_SET(30,"經銷商"),
	PCHOME_SET(20,"PChome營運費用");
	
	private final float percent;				// 百分比
	private final String setItemName;		// 設定項目
	
	private EnumPfbBonusCount(float percent, String setItemName) {
		this.percent = percent;
		this.setItemName = setItemName;
	}

	public float getPercent() {
		return percent;
	}

	public String getSetItemName() {
		return setItemName;
	}
}
