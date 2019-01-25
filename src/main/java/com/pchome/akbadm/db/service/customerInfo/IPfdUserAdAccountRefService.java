package com.pchome.akbadm.db.service.customerInfo;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.manager.ManagerPfpVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;

public interface IPfdUserAdAccountRefService extends IBaseService<PfdUserAdAccountRef, String>{
	public int findPfpCustomerInfoAmount(String pfdCustomerInfoId, String startDate, String endDate);

	public PfdUserAdAccountRef findPfdUserAdAccountRef(String pfdCustomerInfoId);

	public List<PfdUserAdAccountRef> findPfdUserAdAccountRefs(String pfdCustomerInfoId);

	public List<PfdUserAdAccountRef> findPfdUserIdByPfpCustomerInfoId(String pfpCustomerInfoId) throws Exception;

	public Integer deletePfdUserAdAccountRef(String pfpCustomerInfoId);

	public List<ManagerPfpVO> findManagerAccount(ManagerVO managerVO);

	public PfdUserAdAccountRef findPfdUserAdAccountRefByPfpId(String pfpCustomerInfoId);

	public List<PfpCustomerInfo> findPfdUserAdAccountRefByPfdId(List<String> customerInfoIdList);

	public Map<String, PfdUserAdAccountRef> selectPfdUserAdAccountRefMaps();
}
