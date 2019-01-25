package com.pchome.enumerate.bonus;

/**
 * 發票付款方式
 */
public enum EnumInvoicPayType {

	PAY_CHECK("1","支票"),
	PAY_CASH("2","現金"),
	PAY_TEL_TRANS("3","電匯");
	
	private final String type;
	private final String chName;
	
	private EnumInvoicPayType(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}
}
