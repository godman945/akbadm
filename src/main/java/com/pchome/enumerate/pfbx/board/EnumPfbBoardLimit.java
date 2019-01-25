package com.pchome.enumerate.pfbx.board;

public enum EnumPfbBoardLimit {

	Limit(6,"有效期限");

	private final Integer value;
	private final String chName;

	private EnumPfbBoardLimit(Integer value, String chName) {
		this.value = value;
		this.chName = chName;
	}

	public Integer getValue() {
		return value;
	}

	public String getChName() {
		return chName;
	}

}
