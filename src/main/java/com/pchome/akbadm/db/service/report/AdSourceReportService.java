package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdSourceReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public class AdSourceReportService extends BaseService<PfpAdReportVO, String> implements IAdSourceReportService {

	public List<PfpAdReportVO> getAdSourceReportByCondition(Map<String, String> conditionMap) {
		return ((IAdSourceReportDAO) dao).getAdSourceReportByCondition(conditionMap);
	}
	
	public List<PfpAdReportVO> getAdSourceDetalReportByCondition(Map<String, String> conditionMap) {
		return ((IAdSourceReportDAO) dao).getAdSourceDetalReportByCondition(conditionMap);
	}
	
	public List<PfpAdReportVO> getAdSourceReportSumByCondition(Map<String, String> conditionMap) {
		return ((IAdSourceReportDAO) dao).getAdSourceReportSumByCondition(conditionMap);
	}
}
