package com.pchome.akbadm.db.dao.pfbx.account;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;

public interface IPfbxCustomerInfoDAO extends IBaseDAO<PfbxCustomerInfo, String> {
	
	public List<PfbxCustomerInfo> getList_Bykey(String keyword , String category , String accStatus) throws Exception;

	public List<PfbxCustomerInfo> getPfbxCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception;

	public List<PfbxCustomerInfo> findPfbxCustomerInfo(String pfbId);
	
	public List<PfbxCustomerInfo> findQuartzsPfbxCustomerInfo();
	
	public List<PfbxCustomerInfo> getDemoPfbxCustomerInfo();
}
