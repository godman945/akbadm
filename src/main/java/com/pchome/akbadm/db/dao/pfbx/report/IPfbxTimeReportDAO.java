package com.pchome.akbadm.db.dao.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public interface IPfbxTimeReportDAO extends IBaseDAO<PfbxReportVO, String> {
    public List<PfbxReportVO> getPfbxTimeReportByCondition(Map<String, String> conditionMap);
}
