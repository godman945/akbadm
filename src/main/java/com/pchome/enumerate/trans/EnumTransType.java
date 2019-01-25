package com.pchome.enumerate.trans;

public enum EnumTransType {
	
	ADD_MONEY("1","帳戶儲值","+"),
	SPEND_COST("2","廣告花費","-"),
	INVALID_COST("3","無效點選費用","+"),
	REFUND("4","帳戶儲值退款","-"),
	GIFT("5","禮金贈送","+"),
	FEEDBACK_MONEY("6","回饋金","+"),
	LATER_SAVE("7","P幣加值","+"),
	GIFT_FEEDBACK_RETRIEVE("8","禮金&回饋金到期回收","-"),
	LATER_REFUND("9","P幣退款","-");
	
	private final String typeId;
	private final String chName;
	private final String operator;
	
	private EnumTransType(String typeId, String chName, String operator) {
		this.typeId = typeId;
		this.chName = chName;
		this.operator = operator;
	}

	public String getTypeId() {
		return typeId;
	}

	public String getChName() {
		return chName;
	}

	public String getOperator() {
		return operator;
	}
}
