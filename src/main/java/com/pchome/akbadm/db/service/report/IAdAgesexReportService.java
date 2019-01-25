package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpAdAgesexReportVO;

public interface IAdAgesexReportService  extends IBaseService<PfpAdAgesexReportVO, String> {

	public List<PfpAdAgesexReportVO> getAdAgesexReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdAgesexReportVO> getAdAgesexReportTotalByCondition(final Map<String, String> conditionMap);
	
}
