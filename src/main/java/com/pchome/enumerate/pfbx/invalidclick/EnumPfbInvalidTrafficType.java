package com.pchome.enumerate.pfbx.invalidclick;

public enum EnumPfbInvalidTrafficType {

	REPEAT_MEMBERID("1","memid重複"),
	REPEAT_UUIT("2","uuid重複"),
	REPEAT_UUIT_NOT_IPHONE("3","uuid重複(排除iphone)"),
	REPEAT_REMOTEIP("4","remote_ip重複"),
	REPEAT_REFERER("5","referer重複"),
	REPEAT_MOUSEMOVEFLAG("6","mouse_move_flag重複"),
	REPEAT_UUID_AND_REMOTEIP("7","uuid與remote_ip都重複"),
	REPEAT_USERAGENT("8","user_agent重複");


	private final String type;
	private final String chName;
	
	private EnumPfbInvalidTrafficType(String type, String chName){
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
