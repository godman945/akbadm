package com.pchome.akbadm.db.service.user;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.IBaseService;



public interface IPfpUserService extends IBaseService<PfpUser,String>{
	
	public List<PfpUser> getCustomerInfoUsers(String customerInfoId) throws Exception;
	
	public PfpUser getCustomerInfoUser(String userId) throws Exception;
	
	public List<PfpUser> findPfpUser(String pfpCustomerInfoId);
	
	public Integer deletePfpUser(String userId);
	
	public PfpUser findOpenAccountUser(String pfpCustomerInfoId) throws Exception;
}
