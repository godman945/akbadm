package com.pchome.akbadm.db.service.order;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpRefundOrderRelease;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.PfpRefundOrderReleaseVo;

public interface IPfpRefundOrderReleaseService extends IBaseService<PfpRefundOrderRelease, String> {
	
	public int findPfpRefundOrderReleaseCount(Map<String, String> conditionsMap) throws Exception;
	
	public List<PfpRefundOrderReleaseVo> findPfpRefundOrderRelease(Map<String, String> conditionsMap) throws Exception;
	
	// 更新審核後的狀態
	public void updatePfpRefundOrderReleaseStatus(String status, String seq, String verifyUserId, String rejectReason) throws Exception;
	
	
	
	
//	public int findPfdApplyForBusinessCount(Map<String, String> conditionsMap) throws Exception;
//
//	public List<PfdApplyForBusiness> findPfdApplyForBusiness(Map<String, String> conditionsMap,
//			int pageNo, int pageSize) throws Exception;
//
//	public List<PfdApplyForBusinessVO> findPfdApplyForBusinessVO(Map<String, String> conditionsMap,
//			int pageNo, int pageSize) throws Exception;
//
//	/**
//	 * 更新審核後的狀態
//	 */
//	public void updatePfdApplyForBusinessStatus(String status, String seq, String verifyUserId,
//			String rejectReason) throws Exception;
//	
//	public List<PfdApplyForBusiness> findPfdApplyForBusinessStatus(String status) throws Exception;
}
