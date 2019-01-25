package com.pchome.enumerate.pfbx.bonus;

public enum EnumIncomeExpense {

	INCOME("+"),
	EXPENSE("-");
	
	private final String tag;

	private EnumIncomeExpense(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}
	
}
