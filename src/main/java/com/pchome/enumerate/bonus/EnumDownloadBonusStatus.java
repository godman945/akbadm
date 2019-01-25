package com.pchome.enumerate.bonus;

/**
 * 要與前台(pfd) EnumDownloadBonusStatus 一致
 */
public enum EnumDownloadBonusStatus {
	
	YES("Y","開放下載"),
	NO("N","未開放下載");
	
	private final String downloadStatus;
	private final String donwloadName;
	
	private EnumDownloadBonusStatus(String downloadStatus, String donwloadName) {
		this.downloadStatus = downloadStatus;
		this.donwloadName = donwloadName;
	}

	public String getDownloadStatus() {
		return downloadStatus;
	}

	public String getDonwloadName() {
		return donwloadName;
	}
	
	
	
}
