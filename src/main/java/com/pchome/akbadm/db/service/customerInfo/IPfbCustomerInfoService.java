package com.pchome.akbadm.db.service.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfbVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.rmi.manager.PfbAccountVO;

public interface IPfbCustomerInfoService extends IBaseService <PfbxCustomerInfo, String>{
	
	public List<PfbxCustomerInfo> findPfbValidCustomerInfo();
	
	public PfbxCustomerInfo findPfbCustomerInfo(String pfdCustomerInfoId);
	
	public List<PfbAccountVO> findManagerPfbAccount(String memberId, Date startDate, Date endDate);
	
	public List<ManagerPfbVO> findManagerAccount(ManagerVO managerVO);
}
