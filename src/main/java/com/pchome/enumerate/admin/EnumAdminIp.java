package com.pchome.enumerate.admin;

/**
 * 可登入後台系統 IP 位置
 */
public enum EnumAdminIp {
	IP3("10.50.111.3","alex"),
	IP1("10.50.112.44","孟潔"),
	IP2("10.50.111.9","丫瑞");
	
	private final String ip;
	private final String chName;
	
	private EnumAdminIp(String ip, String chName) {
		this.ip = ip;
		this.chName = chName;
	}

	public String getIp() {
		return ip;
	}

	public String getChName() {
		return chName;
	}
		
}
