package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.AdReportVO;

public interface IAdReport2DAO extends IBaseDAO<AdReportVO, String> {
	public List<AdReportVO> getAdReportList(final Map<String, String> conditionMap, final int page, final int pageSize) throws Exception;
	
	public List<AdReportVO> getAdReportListSum(final Map<String, String> conditionMap) throws Exception;
	
	public List<AdReportVO> getAdReportDetailList(final Map<String, String> conditionMap) throws Exception;
}
