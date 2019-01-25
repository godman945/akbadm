package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;

public interface IAdSourceReportService extends IBaseService<PfpAdReportVO, String> {

	public List<PfpAdReportVO> getAdSourceReportByCondition(Map<String, String> conditionMap);
	
	public List<PfpAdReportVO> getAdSourceDetalReportByCondition(Map<String, String> conditionMap);
	
	public List<PfpAdReportVO> getAdSourceReportSumByCondition(Map<String, String> conditionMap);
	
}
