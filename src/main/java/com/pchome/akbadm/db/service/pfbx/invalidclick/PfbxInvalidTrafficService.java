package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.invalidclick.IPfbxInvalidTrafficDAO;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;

public class PfbxInvalidTrafficService extends BaseService<PfbxInvalidTraffic,String> implements IPfbxInvalidTrafficService {
	public List<PfbxInvalidTraffic> findInvalidTraffic(String pfbId) {
		return ((IPfbxInvalidTrafficDAO) dao).findInvalidTraffic(pfbId);
	}
	
	public List<PfbxInvalidTrafficVO> getInvalidTrafficByCondition(final Map<String, String> conditionMap) {
		return ((IPfbxInvalidTrafficDAO) dao).getInvalidTrafficByCondition(conditionMap);
	}
	
	public PfbxInvalidTraffic findInvalidTrafficById(final String invId) {
		
		List<PfbxInvalidTraffic> list = ((IPfbxInvalidTrafficDAO) dao).findInvalidTrafficById(invId);

		if(!list.isEmpty()){
			return list.get(0);
		} else {
			return null;
		}
	}
}
