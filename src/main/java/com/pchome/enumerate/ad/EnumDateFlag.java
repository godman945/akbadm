package com.pchome.enumerate.ad;

public enum EnumDateFlag {
	
	DATE_INDEFINITELY(0, "無限期"), 
	DATE_START(1, "啟用");
	
	private final int flag;
	private final String name;
	
	private EnumDateFlag(int flag, String name) {
		this.flag = flag;
		this.name = name;
	}

	public int getFlag() {
		return flag;
	}

	public String getName() {
		return name;
	}

}
