package com.pchome.enumerate.user;

public enum EnumUserStatus {
	
	CLOSE("0","關閉"),
	START("1","啟用"),
	STOP("2","停權"),
	DELETE("3","刪除"),
	INVITE_PCID("4","尚未接受邀請"),
	INVITE_NOT_PCID("5","尚未接受邀請"),
	APPLY("6","申請中"),
	INVALID("7","無效帳號");	
	
	private final String status;
	private final String chName;
	
	private EnumUserStatus(String status, String chName){
		this.status = status;
		this.chName = chName;
	}

	public String getStatus() {
		return status;
	}

	public String getChName() {
		return chName;
	}
}
