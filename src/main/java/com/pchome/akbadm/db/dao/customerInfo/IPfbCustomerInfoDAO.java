package com.pchome.akbadm.db.dao.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;

public interface IPfbCustomerInfoDAO extends IBaseDAO<PfbxCustomerInfo, String>{

	public List<PfbxCustomerInfo> findPfbValidCustomerInfo();
	
	public List<PfbxCustomerInfo> findPfbCustomerInfo(String pfbCustomerInfoId);
	
	public List<Object> findManagerPfbAccount(String memberId, Date startDate, Date endDate); 
}
