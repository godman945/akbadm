package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCodeConvertRule;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;


public interface IPfpCodeConvertRuleDAO extends IBaseDAO<PfpCodeConvertRule,String>{
	
	public List<Map<String,Object>> getPfpCodeConvertRuleByCondition(PfpConvertTrackingVO convertTrackingVO) throws Exception;
}
