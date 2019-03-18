package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.vo.report.PfpCodeTrackingVO;

public interface ITrackingReportDAO extends IBaseDAO<PfpCodeTracking, String> {

	/**
	 * 再行銷追蹤查詢明細
	 * @param pfpCodeTrackingVO
	 * @return
	 */
	List<Map<String, Object>> getTrackingReportDetail(PfpCodeTrackingVO pfpCodeTrackingVO);
	
}