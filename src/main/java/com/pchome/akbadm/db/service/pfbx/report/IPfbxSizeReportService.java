package com.pchome.akbadm.db.service.pfbx.report;


import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public interface IPfbxSizeReportService extends IBaseService<PfbxReportVO, String>{

	public List<PfbxReportVO> getPfbxSizeReportByCondition(Map<String, String> conditionMap) throws Exception;

}
