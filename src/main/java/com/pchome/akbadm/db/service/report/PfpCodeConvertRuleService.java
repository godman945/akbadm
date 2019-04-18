package com.pchome.akbadm.db.service.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IPfpCodeConvertRuleDAO;
import com.pchome.akbadm.db.pojo.PfpCodeConvertRule;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingRuleVO;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;

public class PfpCodeConvertRuleService extends BaseService<PfpCodeConvertRule,String> implements IPfpCodeConvertRuleService{
	
	public List<PfpConvertTrackingRuleVO> getPfpCodeConvertRuleByCondition(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		List<Map<String,Object>> convertTrackingRuleList = ((IPfpCodeConvertRuleDAO)dao).getPfpCodeConvertRuleByCondition(convertTrackingVO);
		List<PfpConvertTrackingRuleVO> convertTrackingRuleVOList = new ArrayList<PfpConvertTrackingRuleVO>();
		
		if( (!convertTrackingRuleList.isEmpty()) && (convertTrackingRuleList.size()>0) ){
			for (Object object : convertTrackingRuleList) {
				PfpConvertTrackingRuleVO convertTrackingRuleBean = new PfpConvertTrackingRuleVO();
				Map obj = (Map) object;
				
				convertTrackingRuleBean.setConvertRuleId(obj.get("convert_rule_id").toString());	
				convertTrackingRuleBean.setConvertSeq(obj.get("convert_seq").toString());
				convertTrackingRuleBean.setConvertRuleWay(obj.get("convert_rule_way").toString());
				convertTrackingRuleBean.setConvertRuleValue(obj.get("convert_rule_value").toString());
				
				convertTrackingRuleVOList.add(convertTrackingRuleBean);
			}
		}
		
		return convertTrackingRuleVOList;
	}
	
	
}
