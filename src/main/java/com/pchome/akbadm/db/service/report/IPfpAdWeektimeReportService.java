package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpAdTimeReportVO;

public interface IPfpAdWeektimeReportService  extends IBaseService<PfpAdTimeReportVO, String> {

	public List<PfpAdTimeReportVO> getAdWeektimeReportByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdTimeReportVO> getAdWeektimeReportTotalByCondition(final Map<String, String> conditionMap);
	
}
