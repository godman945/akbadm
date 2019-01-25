package com.pchome.akbadm.db.dao.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;

public interface IPfbxInvalidTrafficDAO extends IBaseDAO<PfbxInvalidTraffic, String> {
	public List<PfbxInvalidTraffic> findInvalidTraffic(String pfbId);
	
	public List<PfbxInvalidTrafficVO> getInvalidTrafficByCondition(final Map<String, String> conditionMap);
	
	public List<PfbxInvalidTraffic> findInvalidTrafficById(String invId);
}
