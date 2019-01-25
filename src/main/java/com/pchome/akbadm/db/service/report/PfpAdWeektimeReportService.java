package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IPfpAdWeektimeReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdTimeReportVO;

public class PfpAdWeektimeReportService  extends BaseService<PfpAdTimeReportVO, String> implements IPfpAdWeektimeReportService {

	@Override
	public List<PfpAdTimeReportVO> getAdWeektimeReportByCondition(Map<String, String> conditionMap) {
		return ((IPfpAdWeektimeReportDAO) dao).getAdWeektimeReportByCondition(conditionMap);
	}

	@Override
	public List<PfpAdTimeReportVO> getAdWeektimeReportTotalByCondition(Map<String, String> conditionMap) {
		return ((IPfpAdWeektimeReportDAO) dao).getAdWeektimeReportTotalByCondition(conditionMap);
	}

}
