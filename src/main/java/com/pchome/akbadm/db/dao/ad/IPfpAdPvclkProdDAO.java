package com.pchome.akbadm.db.dao.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkProd;

public interface IPfpAdPvclkProdDAO extends IBaseDAO<PfpAdPvclkProd, String> {

	public List<Map<String,Object>> getProdAdDetailReport(Map<String, String> conditionMap) throws Exception;
	
	public List<Map<String,Object>> getSumProdAdDetailReport(Map<String, String> conditionMap) throws Exception;
	
}
