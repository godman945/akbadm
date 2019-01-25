package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.AdReportVO;

public interface IAdReport2Service extends IBaseService<AdReportVO, String> {
	public List<AdReportVO> getAdReportList(final Map<String, String> conditionMap, final int page, final int pageSize) throws Exception;
	
	public AdReportVO getAdReportListSum(final Map<String, String> conditionMap) throws Exception;
	
	public List<AdReportVO> getAdReportDetailList(final Map<String, String> conditionMap) throws Exception;
}
