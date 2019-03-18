package com.pchome.akbadm.db.service.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdPvclkProd;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ProdAdReportVO;

public interface IPfpAdPvclkProdService extends IBaseService<PfpAdPvclkProd, String> {

	public List<Object> getProdAdDetailReport(Map<String, String> conditionMap) throws Exception;
	
	public ProdAdReportVO getSumProdAdDetailReport(Map<String, String> conditionMap) throws Exception;
	
}
