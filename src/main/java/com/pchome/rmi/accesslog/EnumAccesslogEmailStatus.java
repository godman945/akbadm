package com.pchome.rmi.accesslog;

public enum EnumAccesslogEmailStatus {
	
	NO("0"),			// 不通知
	YES("1"),			// 需通知
	DONE("2");			// 已完成通知
	
	private final String status;
	
	private EnumAccesslogEmailStatus(String status){
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
