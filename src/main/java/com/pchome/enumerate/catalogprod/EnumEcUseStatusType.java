package com.pchome.enumerate.catalogprod;

public enum EnumEcUseStatusType {
	/**
	 * 0:全新
	 */
	New_Goods("0","全新"),		
	/**
	 * 1:二手
	 */
	Used_Goods("1","二手"),
	/**
	 * 2:福利品
	 */
	Welfare_Goods("2","福利品");
	
	private final String type;
	private final String chName;
	
	private EnumEcUseStatusType(String type, String chName) {
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
