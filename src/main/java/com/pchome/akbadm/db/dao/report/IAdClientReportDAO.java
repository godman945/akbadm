package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public interface IAdClientReportDAO extends IBaseDAO<PfpAdReportVO, String> {

	public List<PfpAdReportVO> getAdClientReportByCondition(final Map<String, String> conditionMap);

	public List<PfpAdReportVO> getAdClientDetalReportByCondition(final Map<String, String> conditionMap);
	
}
