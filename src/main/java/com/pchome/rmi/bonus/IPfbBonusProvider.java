package com.pchome.rmi.bonus;

import java.util.List;
import java.util.Map;


public interface IPfbBonusProvider {
	
	
	
	public String downloadPfbBonusFileName(String payDate, String pfbId);
	
	public Map<String,Object> downloadPfbBonusMap(String pfbId, String strYear, String strMonth, String endYear, String endMonth, String payDate);
}
