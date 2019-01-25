package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;

public interface IPfbxInvalidClickService extends IBaseService<PfbxInvalidClickVO,String> {
	
	public List<PfbxInvalidClickVO> getInvalidClickByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdClick> getInvalidData(final Map<String, String> conditionMap) throws Exception;
}
