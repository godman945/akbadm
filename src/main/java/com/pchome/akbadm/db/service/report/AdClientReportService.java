package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdClientReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public class AdClientReportService extends BaseService<PfpAdReportVO, String> implements IAdClientReportService {

	public List<PfpAdReportVO> getAdClientReportByCondition(Map<String, String> conditionMap) {
		return ((IAdClientReportDAO) dao).getAdClientReportByCondition(conditionMap);
	}
	
	public List<PfpAdReportVO> getAdClientDetalReportByCondition(Map<String, String> conditionMap) {
		return ((IAdClientReportDAO) dao).getAdClientDetalReportByCondition(conditionMap);
	}
}
