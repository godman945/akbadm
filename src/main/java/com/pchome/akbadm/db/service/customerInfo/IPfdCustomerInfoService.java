package com.pchome.akbadm.db.service.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfdVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.rmi.manager.PfdAccountVO;

public interface IPfdCustomerInfoService extends IBaseService <PfdCustomerInfo, String>{
	
	public List<PfdCustomerInfo> findPfdValidCustomerInfo();
	
	public PfdCustomerInfo findPfdCustomerInfo(String pfdCustomerInfoId);
	
	public List<PfdAccountVO> findManagerPfdAccount(String memberId, Date startDate, Date endDate);
	
	public List<ManagerPfdVO> findManagerAccount(ManagerVO managerVO);
}
