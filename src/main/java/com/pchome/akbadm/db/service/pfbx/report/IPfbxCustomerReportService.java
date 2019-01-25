package com.pchome.akbadm.db.service.pfbx.report;


import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;

public interface IPfbxCustomerReportService extends IBaseService<PfbxCustomerReportVo, String>{

	public List<PfbxCustomerReportVo> getPfbxCustomerReportByCondition(Map<String, String> conditionMap, Map<String, String> conditionMap1) throws Exception;

}
