package com.pchome.rmi.bonus;

import java.util.Map;


public interface IBonusProvider {	
	
	public Map<String,Object> bonusDetailBillMap(String pfdCustomerInfoId, EnumPfdAccountPayType accountPayType, int year, int month);
}
