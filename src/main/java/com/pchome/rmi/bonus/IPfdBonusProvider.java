package com.pchome.rmi.bonus;

import java.util.Map;

public interface IPfdBonusProvider {

	public Map<String,Object> pfdBonusBillMap(String pfdId, int year, int month) throws Exception;
}
