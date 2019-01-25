package com.pchome.enumerate.video;

public enum EnumDownloadStatus {
	DOWNLOAD_PENDING(0,"未下載"),
	DOWNLOAD_SUCCESS(1,"下載完成"),
	DOWNLOAD_START(2,"下載中"),
	MP4_FAIL(3,"mp4下載失敗"),
	WEBM_FAIL(4,"webm下載失敗"),
	DOWNLOAD_FAIL(5,"下載失敗"),
	SYNC_FAIL(6,"搬移失敗");
	
	private final int status;
	private final String message;
	
	private EnumDownloadStatus(int status, String message){
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	
}
