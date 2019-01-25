package com.pchome.enumerate.bonus;

/**
 * 要與前台(pfd) EnumApplyBonusStatus 一致
 */
public enum EnumApplyBonusStatus {

	YES("Y","已請款"),
	NO("N","未請款");
	
	private final String applyStatus;
	private final String applyName;
	
	private EnumApplyBonusStatus(String applyStatus, String applyName) {
		this.applyStatus = applyStatus;
		this.applyName = applyName;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public String getApplyName() {
		return applyName;
	}
	
	
}
