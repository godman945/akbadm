package com.pchome.enumerate.order;

public enum EnumPfpRefundOrderStatus {
	
	SUCCESS("Y", "退款成功"),
	FAIL("N", "退款失敗"),
	NOT_REFUND("F", "尚未退款");
	
	private final String status;
	private final String chName;
	
	private EnumPfpRefundOrderStatus(String status, String chName) {
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
