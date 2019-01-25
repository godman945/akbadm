package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdAgesexReportVO;

public interface IAdAgesexReportDAO extends IBaseDAO<PfpAdAgesexReportVO, String> {

	public List<PfpAdAgesexReportVO> getAdAgesexReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdAgesexReportVO> getAdAgesexReportTotalByCondition(final Map<String, String> conditionMap);
}
