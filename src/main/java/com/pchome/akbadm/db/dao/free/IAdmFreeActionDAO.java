package com.pchome.akbadm.db.dao.free;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.vo.ad.AdmFreeActionVO;

public interface IAdmFreeActionDAO extends IBaseDAO<AdmFreeAction, String>{
	
	public List<AdmFreeAction> findFreeAction(String actionId);

	public List<AdmFreeAction> findFreeAction(Map<String, String> conditionMap) throws Exception;
	
	public List<AdmFreeActionVO> getAdmFreeActionData(final Map<String, String> conditionMap);
}
