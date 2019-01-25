package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdWebsiteReportVO;

public interface IAdWebsiteReportDAO extends IBaseDAO<PfpAdWebsiteReportVO, String> {

	public List<PfpAdWebsiteReportVO> getAdWebsiteReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdWebsiteReportVO> getAdWebsiteReportSumByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdWebsiteReportVO> getAdWebsiteDetalReportByCondition(final Map<String, String> conditionMap);
	
}
