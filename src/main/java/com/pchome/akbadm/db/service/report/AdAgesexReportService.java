package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdAgesexReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdAgesexReportVO;

public class AdAgesexReportService  extends BaseService<PfpAdAgesexReportVO, String> implements IAdAgesexReportService {

	@Override
	public List<PfpAdAgesexReportVO> getAdAgesexReportByCondition(Map<String, String> conditionMap) {
		return ((IAdAgesexReportDAO) dao).getAdAgesexReportByCondition(conditionMap);
	}

	@Override
	public List<PfpAdAgesexReportVO> getAdAgesexReportTotalByCondition(Map<String, String> conditionMap) {
		return ((IAdAgesexReportDAO) dao).getAdAgesexReportTotalByCondition(conditionMap);
	}

}
