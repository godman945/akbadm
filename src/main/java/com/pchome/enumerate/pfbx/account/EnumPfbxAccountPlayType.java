package com.pchome.enumerate.pfbx.account;

public enum EnumPfbxAccountPlayType {

	PLAYALL("0", "全播"),
	PLAYALLOW("1", "只播白名單");

	private final String type;
	private final String chName;
	
	private EnumPfbxAccountPlayType(String type, String chName) {
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
