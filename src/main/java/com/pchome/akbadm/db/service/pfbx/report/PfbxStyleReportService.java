package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxStyleReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxStyleReportService extends BaseService<PfbxReportVO, String> implements IPfbxStyleReportService {

	public List<PfbxReportVO> getPfbxStyleReportByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfbxStyleReportDAO) dao).getPfbxStyleReportByCondition(conditionMap);
	}
	
}
