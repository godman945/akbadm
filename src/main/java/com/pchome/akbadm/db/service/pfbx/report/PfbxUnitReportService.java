package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxUnitReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxUnitReportService extends BaseService<PfbxReportVO, String> implements IPfbxUnitReportService {

	public List<PfbxReportVO> getPfbxUnitReportByCondition(Map<String, String> conditionMap, Map<String, String> conditionMap1) throws Exception {
		return ((IPfbxUnitReportDAO) dao).getPfbxUnitReportByCondition(conditionMap, conditionMap1);
	}
	
}
