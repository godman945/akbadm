package com.pchome.akbadm.db.dao.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdDetail;

public interface IPfpAdDetailDAO extends IBaseDAO<PfpAdDetail, String> {
	
	public PfpAdDetail findAdContent(String adSeq, String defineAdSeq) throws Exception;
	
	public List<Object> getAdPriceList(String adPoolSeq, float sysprice, String today) throws Exception;

	public List<PfpAdDetail> getPfpAdDetails(String adDetailSeq, String adSeq, String adPoolSeq, String defineAdSeq) throws Exception;
	
	public List<Map<String,Object>> getProdAdName(String adSeq) throws Exception;
}
