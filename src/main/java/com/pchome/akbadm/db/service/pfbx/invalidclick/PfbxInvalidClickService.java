package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.invalidclick.IPfbxInvalidClickDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;

public class PfbxInvalidClickService extends BaseService<PfbxInvalidClickVO,String> implements IPfbxInvalidClickService {
	
	@Override
	public List<PfbxInvalidClickVO> getInvalidClickByCondition(final Map<String, String> conditionMap){
		return ((IPfbxInvalidClickDAO)dao).getInvalidClickByCondition(conditionMap);
	}
	
	@Override
	public List<PfpAdClick> getInvalidData(final Map<String, String> conditionMap) throws Exception{
		return ((IPfbxInvalidClickDAO)dao).getInvalidData(conditionMap);
	}
}
