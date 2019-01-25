package com.pchome.akbadm.db.dao.applyFor;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;

public interface IPfdApplyForBusinessDAO extends IBaseDAO<PfdApplyForBusiness, String> {

	public int findPfdApplyForBusinessCount(Map<String, String> conditionsMap) throws Exception;

	public List<PfdApplyForBusiness> findPfdApplyForBusiness(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception;

	/**
	 * 更新審核後的狀態
	 */
	public void updatePfdApplyForBusinessStatus(String status, String seq, String verifyUserId,
			String rejectReason) throws Exception;
	
	public List<PfdApplyForBusiness> findPfdApplyForBusinessStatus(String status) throws Exception;
}
