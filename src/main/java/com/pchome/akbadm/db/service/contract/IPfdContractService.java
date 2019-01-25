package com.pchome.akbadm.db.service.contract;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfdContractService extends IBaseService<PfdContract, String>{

	public List<PfdContract> findPfdContract();
	
	//public List<PfdContract> findValidPfdCustomerInfo();
	
	public PfdContract findPfdContract(String pfdContractId);

	public List<PfdContract> findPfdContract(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception;
	
	public List<PfdContract> findValidPfdContract();
	
	public List<PfdContract> findValidPfdContract(Date recordDate);
	
	public List<PfdContract> checkPfdContractOverlap(String pfdCustomerInfo, Date newStartDate, Date newEndDate)throws Exception;
}
