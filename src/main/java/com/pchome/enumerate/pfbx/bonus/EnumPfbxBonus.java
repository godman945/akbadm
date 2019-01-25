package com.pchome.enumerate.pfbx.bonus;

public enum EnumPfbxBonus {

	DEFAULT(EnumPfbxBonusSet.OPEN,100);			// pfb 初始設定
	
	private EnumPfbxBonusSet enumBonusSet;		// Enum 分潤狀態設定
	private int percent;						// pfb 收益百分比


	private EnumPfbxBonus(EnumPfbxBonusSet enumBonusSet, int percent) {
		this.enumBonusSet = enumBonusSet;
		this.percent = percent;
	}

	public EnumPfbxBonusSet getEnumBonusSet() {
		return enumBonusSet;
	}

	public void setEnumBonusSet(EnumPfbxBonusSet enumBonusSet) {
		this.enumBonusSet = enumBonusSet;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
	
}
