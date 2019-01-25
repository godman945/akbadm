package com.pchome.akbadm.db.service.pfbx.account;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public interface IPfbxCustomerInfoService extends IBaseService<PfbxCustomerInfo, String>{
	
	public List<PfbAdUrlListVO> getPfbAdUrlList(String keyword , String category , String accStatus) throws Exception;

	public List<PfbxCustomerInfo> getPfbxCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception;

	public PfbxCustomerInfo findPfbxCustomerInfo(String pfbId);
	
	public List<PfbxCustomerInfo> findValidPfbxCustomerInfo();
	
	public List<PfbxCustomerInfo> getDemoPfbxCustomerInfo();
}
