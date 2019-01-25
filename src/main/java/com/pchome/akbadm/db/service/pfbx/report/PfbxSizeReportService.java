package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxSizeReportDAO;
import com.pchome.akbadm.db.dao.pfbx.report.IPfbxTimeReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxSizeReportService extends BaseService<PfbxReportVO, String> implements IPfbxSizeReportService {

	public List<PfbxReportVO> getPfbxSizeReportByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfbxSizeReportDAO) dao).getPfbxSizeReportByCondition(conditionMap);
	}
	
}
