package com.pchome.akbadm.db.dao.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;

public interface IPfbxInvalidClickDAO extends IBaseDAO<PfbxInvalidClickVO, String> {
	
	public List<PfbxInvalidClickVO> getInvalidClickByCondition(final Map<String, String> conditionMap);
	
	public List<PfpAdClick> getInvalidData(final Map<String, String> conditionMap) throws Exception;
}
