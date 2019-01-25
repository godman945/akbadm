package com.pchome.akbadm.db.dao.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;

public interface IPfbxCustomerReportDAO extends IBaseDAO<PfbxCustomerReportVo, String> {
    public List<PfbxCustomerReportVo> getPfbxCustomerReportByCondition(Map<String, String> conditionMap, Map<String, String> conditionMap1);
}
