package com.pchome.akbadm.db.dao.customerInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;

public interface IPfdCustomerInfoDAO extends IBaseDAO<PfdCustomerInfo, String>{

	public List<PfdCustomerInfo> findPfdValidCustomerInfo();
	
	public List<PfdCustomerInfo> findPfdCustomerInfo(String pfdCustomerInfoId);

	// 2014-04-24
	public HashMap<String, PfdCustomerInfo> getPfdCustomerInfoBySeqList(List<String> customerInfoIdList) throws Exception;	
	
	public List<Object> findManagerPfdAccount(String memberId, Date startDate, Date endDate); 
	
}
