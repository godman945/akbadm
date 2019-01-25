package com.pchome.akbadm.db.service.contract;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.contract.IPfdContractDAO;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.contract.EnumContractStatus;

public class PfdContractService extends BaseService<PfdContract, String> implements IPfdContractService{

	
	public List<PfdContract> findPfdContract() {
		
		return ((IPfdContractDAO)dao).findPfdContract();
	}
	
	//public List<PfdContract> findValidPfdCustomerInfo() {
	//	
	//	return ((IPfdContractDAO)dao).findValidPfdCustomerInfo();
	//}
	
	public PfdContract findPfdContract(String pfdContractId) {
		List<PfdContract> list = ((IPfdContractDAO)dao).findPfdContract(pfdContractId);
		
		if(list.isEmpty()){
			return null; 
		}else{
			return list.get(0);
		}		
	}

	public List<PfdContract> findPfdContract(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception {
		return ((IPfdContractDAO) dao).findPfdContract(conditionsMap, pageNo, pageSize);
	}
	
	public List<PfdContract> findValidPfdContract() {
		return ((IPfdContractDAO)dao).findValidPfdContract();
	}
	
	public List<PfdContract> findValidPfdContract(Date recordDate) {
		return ((IPfdContractDAO)dao).findValidPfdContract(recordDate);
	}
	
	public List<PfdContract> checkPfdContractOverlap(String pfdCustomerInfo, Date newStartDate, Date newEndDate)throws Exception{
		return ((IPfdContractDAO)dao).checkPfdContractOverlap(pfdCustomerInfo, newStartDate, newEndDate);
	}
	
}
