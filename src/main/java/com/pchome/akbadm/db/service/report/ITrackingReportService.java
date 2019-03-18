package com.pchome.akbadm.db.service.report;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpCodeTrackingVO;

public interface ITrackingReportService extends IBaseService<PfpCodeTracking, String> {

	/**
	 * 再行銷追蹤查詢明細
	 * @param pfpCodeTrackingVO
	 * @return
	 * @throws Exception 
	 */
	List<PfpCodeTrackingVO> getTrackingReportDetail(PfpCodeTrackingVO pfpCodeTrackingVO) throws Exception;

}