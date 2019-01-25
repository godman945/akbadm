package com.pchome.enumerate.pfbx.bonus;

public enum EnumAdmBonusDetailItem {

	//INCOME_PFD("1","PFD 收入",EnumIncomeExpense.INCOME),
	INCOME_PFP("1","pchome 營運 ",EnumIncomeExpense.INCOME),
	INCOME_PFB("2","PFB 收入",EnumIncomeExpense.INCOME),
	EXPENSE_PFD("3","PFD 佣金",EnumIncomeExpense.EXPENSE),
	EXPENSE_PFB("4","PFB 分潤",EnumIncomeExpense.EXPENSE);
	
	private final String itemId;
	private final String itemName;
	private final EnumIncomeExpense incomeExpense;
	
	private EnumAdmBonusDetailItem(String itemId, String itemName,
			EnumIncomeExpense incomeExpense) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.incomeExpense = incomeExpense;
	}

	public String getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public EnumIncomeExpense getIncomeExpense() {
		return incomeExpense;
	}
		
}
