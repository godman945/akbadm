package com.pchome.akbadm.db.service.user;

import java.util.List;

import com.pchome.akbadm.db.dao.user.IPfpUserDAO;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.BaseService;

public class PfpUserService extends BaseService<PfpUser,String> implements IPfpUserService{
	
	public List<PfpUser> getCustomerInfoUsers(String customerInfoId) throws Exception{		
		return ((IPfpUserDAO)dao).getCustomerInfoUsers(customerInfoId);
	}

	public PfpUser getCustomerInfoUser(String userId) throws Exception{
		return ((IPfpUserDAO)dao).getCustomerInfoUser(userId);
	}
	
	public List<PfpUser> findPfpUser(String pfpCustomerInfoId) {
		return ((IPfpUserDAO)dao).findPfpUser(pfpCustomerInfoId);
	}
	
	public Integer deletePfpUser(String userId) {
		return ((IPfpUserDAO)dao).deletePfpUser(userId);
	}
	
	public PfpUser findOpenAccountUser(String pfpCustomerInfoId) throws Exception{
		
		List<PfpUser> users = ((IPfpUserDAO)dao).findOpenAccountUser(pfpCustomerInfoId);
		
		if(!users.isEmpty()){
			return users.get(0);
		}else{
			return null;
		}
	}
	
}
