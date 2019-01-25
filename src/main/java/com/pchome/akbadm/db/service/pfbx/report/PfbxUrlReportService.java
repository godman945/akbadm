package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxUrlReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxUrlReportService extends BaseService<PfbxReportVO, String> implements IPfbxUrlReportService {

	public List<PfbxReportVO> getPfbxUrlReportByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfbxUrlReportDAO) dao).getPfbxUrlReportByCondition(conditionMap);
	}
	
	public List<PfbxReportVO> getPfbxUrlDetalReportByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfbxUrlReportDAO) dao).getPfbxUrlDetalReportByCondition(conditionMap);
	}
	
}
