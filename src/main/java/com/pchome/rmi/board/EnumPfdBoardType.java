package com.pchome.rmi.board;

public enum EnumPfdBoardType {

	SYSTEM("0", "系統公告"),
	ACTION("1", "活動公告"),
	VERIFY("2", "審核公告"),
	AD("3", "廣告公告"),
	FINANCE("4", "帳務公告"),
	REMIND("5", "帳戶公告");

	private final String type;
	private final String chName;

	private EnumPfdBoardType(String type, String chName) {
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
