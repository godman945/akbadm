package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public interface IAdClientReportService  extends IBaseService<PfpAdReportVO, String> {

	public List<PfpAdReportVO> getAdClientReportByCondition(Map<String, String> conditionMap);
	
	public List<PfpAdReportVO> getAdClientDetalReportByCondition(Map<String, String> conditionMap);
	
}
