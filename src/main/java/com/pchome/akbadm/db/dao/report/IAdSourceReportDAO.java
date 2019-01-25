package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public interface IAdSourceReportDAO extends IBaseDAO<PfpAdReportVO, String> {

	public List<PfpAdReportVO> getAdSourceReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdReportVO> getAdSourceDetalReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdReportVO> getAdSourceReportSumByCondition(final Map<String, String> conditionMap);
}
