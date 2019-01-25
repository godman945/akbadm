package com.pchome.enumerate.ad;

public enum EnumAdGiftStatus {
	
	Close(0, "關閉", "關閉"),
	Open(1, "開啟", "開啟"),
	Paying(2, "付款中", "付款中"),
	PayFail(3, "付款失敗", "付款失敗"),
	PaySuccess(4, "付款成功", "付款成功"),
	TestSNO(5, "測試序號", "測試序號");
	
	private final int statusId;
	private final String statusDesc;
	private final String statusRemark;
	
	private EnumAdGiftStatus(int statusId, String statusDesc, String statusRemark){
		this.statusId = statusId;
		this.statusDesc = statusDesc;
		this.statusRemark = statusRemark;
	}

	public int getStatusId() {
		return statusId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public String getStatusRemark() {
		return statusRemark;
	}
}
