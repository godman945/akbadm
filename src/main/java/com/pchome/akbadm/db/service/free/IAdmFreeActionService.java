package com.pchome.akbadm.db.service.free;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ad.AdmFreeActionVO;
import com.pchome.enumerate.factory.EnumActivityType;

public interface IAdmFreeActionService extends IBaseService<AdmFreeAction, String>{

	public AdmFreeAction findFreeAction(EnumActivityType enumActivityType);

	public AdmFreeAction findFreeAction(String actionId);
	
	public List<AdmFreeAction> findFreeAction(Map<String, String> conditionMap) throws Exception;
	
	public List<AdmFreeActionVO> getAdmFreeActionData(final Map<String, String> conditionMap);
}
