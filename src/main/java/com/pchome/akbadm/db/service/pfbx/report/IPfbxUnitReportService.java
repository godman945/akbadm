package com.pchome.akbadm.db.service.pfbx.report;


import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public interface IPfbxUnitReportService extends IBaseService<PfbxReportVO, String>{

	public List<PfbxReportVO> getPfbxUnitReportByCondition(Map<String, String> conditionMap, Map<String, String> conditionMap1) throws Exception;

}
