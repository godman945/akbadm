package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxCustomerReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;

public class PfbxCustomerReportService extends BaseService<PfbxCustomerReportVo, String> implements IPfbxCustomerReportService {

	public List<PfbxCustomerReportVo> getPfbxCustomerReportByCondition(Map<String, String> conditionMap, Map<String, String> conditionMap1) throws Exception {
		return ((IPfbxCustomerReportDAO) dao).getPfbxCustomerReportByCondition(conditionMap, conditionMap1);
	}
	
}
