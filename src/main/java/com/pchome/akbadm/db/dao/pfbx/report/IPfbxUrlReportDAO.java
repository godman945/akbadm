package com.pchome.akbadm.db.dao.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public interface IPfbxUrlReportDAO extends IBaseDAO<PfbxReportVO, String> {
	public List<PfbxReportVO> getPfbxUrlReportByCondition(Map<String, String> conditionMap);
	
	public List<PfbxReportVO> getPfbxUrlDetalReportByCondition(Map<String, String> conditionMap);
}
