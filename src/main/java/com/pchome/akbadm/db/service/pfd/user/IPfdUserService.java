package com.pchome.akbadm.db.service.pfd.user;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfdUserService extends IBaseService<PfdUser, String>{

	public List<PfdUser> getPfdUserByCondition(Map<String, String> conditionMap) throws Exception;
	
	public List<PfdUser> findRootPfdUser(String pfdCustomerInfoId) throws Exception;

}
