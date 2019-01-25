package com.pchome.rmi.board;

public enum EnumBoardType {

	ALL("0","最新公告"),
	FINANCE("1","帳務公告"),
	ACCOUNT("7","帳戶公告"),
	ACTION("4","活動公告"),
	AD("6","廣告公告"),
	VERIFY("2","審核公告"),
	SYSTEM("3","服務公告");
	//WINNING("5","中獎公告");

	private final String type;
	private final String chName;

	private EnumBoardType(String type, String chName) {
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
