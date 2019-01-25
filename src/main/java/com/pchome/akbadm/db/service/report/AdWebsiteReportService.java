package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdWebsiteReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdWebsiteReportVO;

public class AdWebsiteReportService extends BaseService<PfpAdWebsiteReportVO, String> implements IAdWebsiteReportService {

	@Override
	public List<PfpAdWebsiteReportVO> getAdWebsiteReportByCondition(Map<String, String> conditionMap) {
		return ((IAdWebsiteReportDAO) dao).getAdWebsiteReportByCondition(conditionMap);
	}

	@Override
	public List<PfpAdWebsiteReportVO> getAdWebsiteReportSumByCondition(Map<String, String> conditionMap) {
		return ((IAdWebsiteReportDAO) dao).getAdWebsiteReportSumByCondition(conditionMap);
	}

	@Override
	public List<PfpAdWebsiteReportVO> getAdWebsiteDetalReportByCondition(Map<String, String> conditionMap) {
		return ((IAdWebsiteReportDAO) dao).getAdWebsiteDetalReportByCondition(conditionMap);
	}

}
