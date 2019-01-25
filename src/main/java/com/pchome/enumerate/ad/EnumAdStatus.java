package com.pchome.enumerate.ad;

public enum EnumAdStatus {
	
	UnDone(0, "未完成", "未完成"),
	NoVerify(1, "未審核", "未審核"),
	Verify_sys_pass(2, "審核中-pass", "審核中"),
	Reject(3, "已拒絕", "已拒絕"),
	Open(4, "開啟", "開啟"),
	Broadcast(5, "走期中", "走期中"),
	Illegalize(6, "違規下架", "違規下架"),
	Waitbroadcast(7, "待播放", "待播放"),
	End(8, "已結束", "已結束"),
	Pause(9, "已暫停", "已暫停"),
	Close(10, "已關閉", "已關閉"),
	NoMoney(11, "餘額不足", "餘額不足"),
	ReachUpLimit(12, "已達上限", "已達上限"),
	Verify_sys_regect(13, "審核中-reject", "審核中");
	
	private final int statusId;
	private final String statusDesc;
	private final String statusRemark;
	
	private EnumAdStatus(int statusId, String statusDesc, String statusRemark){
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
