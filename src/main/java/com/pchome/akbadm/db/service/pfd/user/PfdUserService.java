package com.pchome.akbadm.db.service.pfd.user;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfd.user.IPfdUserDAO;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.service.BaseService;

public class PfdUserService extends BaseService<PfdUser, String> implements IPfdUserService {

	public List<PfdUser> getPfdUserByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfdUserDAO) dao).getPfdUserByCondition(conditionMap);
	}
	
	public List<PfdUser> findRootPfdUser(String pfdCustomerInfoId) throws Exception{
		log.info(">>> pfdCustomerInfoId = "+pfdCustomerInfoId);
		
		return ((IPfdUserDAO) dao).findRootPfdUser(pfdCustomerInfoId);
	}
}
