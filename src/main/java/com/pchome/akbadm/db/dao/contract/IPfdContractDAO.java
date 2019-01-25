package com.pchome.akbadm.db.dao.contract;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdContract;

public interface IPfdContractDAO extends IBaseDAO<PfdContract, String>{
	
	public List<PfdContract> findPfdContract();
	
	//public List<PfdContract> findValidPfdCustomerInfo();
	
	public List<PfdContract> findPfdContract(String pfdContractId);

	public List<PfdContract> findPfdContract(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception;
	
	public List<PfdContract> findValidPfdContract();
	
	public List<PfdContract> findValidPfdContract(Date recordDate);
	
	public List<PfdContract> checkPfdContractOverlap(String pfdCustomerInfo, Date newStartDate, Date newEndDate)throws Exception;
}
