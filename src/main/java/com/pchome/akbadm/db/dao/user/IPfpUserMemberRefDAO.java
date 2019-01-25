package com.pchome.akbadm.db.dao.user;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpUserMemberRef;

public interface IPfpUserMemberRefDAO extends IBaseDAO<PfpUserMemberRef, String>{
	
	public PfpUserMemberRef getUserMemberRef(String userId) throws Exception;
	
	public Integer deletePfpUserMemberRef(String pfpCustomerInfoId);
}
