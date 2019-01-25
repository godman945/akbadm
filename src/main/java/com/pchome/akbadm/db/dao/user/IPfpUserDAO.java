package com.pchome.akbadm.db.dao.user;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpUser;

public interface IPfpUserDAO extends IBaseDAO<PfpUser, String>{
	
	public List<PfpUser> getCustomerInfoUsers(String customerInfoId) throws Exception;
	
	public PfpUser getCustomerInfoUser(String userId) throws Exception;

	public List<PfpUser> findPfpUser(String pfpCustomerInfoId);
	
	public Integer deletePfpUser(String userId);
	
	public List<PfpUser> findOpenAccountUser(String pfpCustomerInfoId) throws Exception;
}
