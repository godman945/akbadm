package com.pchome.enumerate.pfd.bonus;

public enum EnumPfdBonusBill {
	
	APPLY("1","已付PChome廣告費/需寄回佣金發票與請款確認單","已付PChome廣告費/需寄回佣金發票與請款確認單"),
	NOT_APPLY("2","尚未請款","尚未請款"),
	PAY_FAIL("3","佣金請款失敗","佣金請款失敗"),
	PAY_MONEY("4","佣金請款成功","佣金請款成功");
	
	private final String status;
	private final String advanceChName;
	private final String laterChName;
	
	private EnumPfdBonusBill(String status, String advanceChName,String laterChName) {
		this.status = status;
		this.advanceChName = advanceChName;
		this.laterChName = laterChName;
	}

	public String getStatus() {
		return status;
	}

	public String getAdvanceChName() {
		return advanceChName;
	}

	public String getLaterChName() {
		return laterChName;
	}
		
}
