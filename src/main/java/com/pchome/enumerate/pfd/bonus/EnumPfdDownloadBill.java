package com.pchome.enumerate.pfd.bonus;

public enum EnumPfdDownloadBill {

	YES("Y","開放下載"),
	NO("N","未開放下載");
	
	private final String status;
	private final String chName;
	
	private EnumPfdDownloadBill(String status, String chName) {
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
