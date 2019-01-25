package com.pchome.rmi.bonus;

/**
 * 要與前台(pfd) EnumPfdAccountPayType 一致
 */
public enum EnumPfdAccountPayType {

	ADVANCE("1","預付"),		// 預付
	LATER("2","後付"),		// 後付
	BOTH("3","預付和後付");	// 預付和後付都有		
	
	private final String payType;
	private final String payName;

	private EnumPfdAccountPayType(String payType, String payName) {
		this.payType = payType;
		this.payName = payName;
	}

	public String getPayType() {
		return payType;
	}

	public String getPayName() {
		return payName;
	}
	
	
}
