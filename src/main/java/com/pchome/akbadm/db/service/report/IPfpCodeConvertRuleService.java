package com.pchome.akbadm.db.service.report;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpCodeConvertRule;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingRuleVO;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;

public interface IPfpCodeConvertRuleService extends IBaseService<PfpCodeConvertRule,String>{
	
	public List<PfpConvertTrackingRuleVO>  getPfpCodeConvertRuleByCondition(PfpConvertTrackingVO convertTrackingVO) throws Exception;
}