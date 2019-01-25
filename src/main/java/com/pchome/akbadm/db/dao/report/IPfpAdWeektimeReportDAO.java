package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdTimeReportVO;

public interface IPfpAdWeektimeReportDAO extends IBaseDAO<PfpAdTimeReportVO, String> {

	public List<PfpAdTimeReportVO> getAdWeektimeReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdTimeReportVO> getAdWeektimeReportTotalByCondition(final Map<String, String> conditionMap);
}
