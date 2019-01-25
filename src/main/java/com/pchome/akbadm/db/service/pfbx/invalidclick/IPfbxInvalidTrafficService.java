package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;

public interface IPfbxInvalidTrafficService extends IBaseService<PfbxInvalidTraffic,String> {
	public List<PfbxInvalidTraffic> findInvalidTraffic(String pfbId);
	
	public List<PfbxInvalidTrafficVO> getInvalidTrafficByCondition(final Map<String, String> conditionMap);
	
	public PfbxInvalidTraffic findInvalidTrafficById(final String invId);
}
