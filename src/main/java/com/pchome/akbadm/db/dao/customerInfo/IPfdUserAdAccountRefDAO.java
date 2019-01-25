package com.pchome.akbadm.db.dao.customerInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;

public interface IPfdUserAdAccountRefDAO extends IBaseDAO<PfdUserAdAccountRef, String>{

	public List<PfdUserAdAccountRef> findPfpCustomerInfo(String pfdCustomerInfoId,
			Date startDate, Date endDate);
	
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRef(String pfdCustomerInfoId);
	
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRef(String pfdCustomerInfoId, List<String> status);

	public List<PfdUserAdAccountRef> findPfdUserIdByPfpCustomerInfoId(String pfpCustomerInfoId)
			throws Exception;
	
	public Integer deletePfdUserAdAccountRef(String pfpCustomerInfoId);
	
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRefs();
	
	public HashMap<String, PfdUserAdAccountRef> getPfdUserAdAccountRefBySeqList(List<String> customerInfoIdList);
	
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRefByPfpId(String pfpCustomerInfoId);
	
	public List<PfpCustomerInfo> findPfdUserAdAccountRefByPfdId(List<String> customerInfoIdList);
}
