package com.pchome.akbadm.db.dao.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;

public interface IPfpAdKeywordDAO extends IBaseDAO<PfpAdKeyword, String>{
	
	public List<PfpAdKeyword> findPfpAdKeywordByKeyword(String keyword) throws Exception;
	
	public PfpAdKeyword findPfpAdKeywordBySeq(String adKeywordSeq) throws Exception;
	
	public List<Object> getAdKeywordPriceList(String keyword, float sysprice, String today) throws Exception;

	public List<Object> getAdKeywordReport(Map<String,String> adKeywordViewConditionMap) throws Exception;
	    
	public int getAdKeywordReportSize(Map<String,String> adKeywordViewConditionMap) throws Exception;
}
